package com.quark.risk.saccr.service;

import com.quark.core.exception.ApplicationException;
import com.quark.risk.saccr.model.AssetClass;
import com.quark.risk.saccr.model.BuySell;
import com.quark.risk.saccr.model.Collateral;
import com.quark.risk.saccr.model.CollateralLookupKey;
import com.quark.risk.saccr.model.ContractType;
import com.quark.risk.saccr.model.SaccrMarketQuote;
import com.quark.risk.saccr.model.ProductNames;
import com.quark.risk.saccr.model.PutCall;
import com.quark.risk.saccr.model.RequiredTradeAttributes;
import com.quark.risk.saccr.model.SettleType;
import com.quark.risk.saccr.model.SingleNameOrIndex;
import com.quark.risk.saccr.model.SupervisoryParameters;
import com.quark.risk.saccr.results.SACCRResults;
import com.quark.risk.saccr.results.Trade;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Service
@Scope("prototype")
public class SACCRCalculator {

	@Value("${mpor.init.value:10}")
	private int mporInitValue;

	@Value("${mpor.broker.client.init.value:5}")
	private int mporBrokerClientInitValue;

	@Value("${calendar.days.in.a.year:365}")
	private int daysInAYear;

	@Value("${business.days.in.a.year:250}")
	private int businessDaysInAYear;

	@Value("${saccr.alpha:1.4}")
	private double alpha;

	private NormalDistribution normsdist = new NormalDistribution();

	private Date valueDate;
	private String baseCurrency;

	private List<Collateral> collateral;
	private List<com.quark.risk.saccr.model.Trade> trades;
	private List<SupervisoryParameters> supervisoryParameters;
	private Map<String, SaccrMarketQuote> marketData;
	private Map<String, RequiredTradeAttributes> reqTradeAttrMap;
	private Map<CollateralLookupKey, Collateral> collateralMap;

	private SACCRResults results = new SACCRResults();

	public SACCRResults calculate() {
		if (!validate()) {
			return results;
		}

		List<String> tradeList = new ArrayList<>();
		// Map<String, Counterparty> counterparties = new HashMap<>();
		for (com.quark.risk.saccr.model.Trade t : trades) {
			/*
			 * Counterparty c = counterparties.get(t.getLegalEntity()); if (c ==
			 * null) { c = new Counterparty();
			 * counterparties.put(t.getLegalEntity(), c); }
			 */
			Trade trade;
			try {
				trade = buildTrade(t);
			} catch (ApplicationException e) {
				log.error(e.getMessage(), e);
				continue;
			}
			tradeList.add(trade.getCommaSeperatedString());
			// c.addTrade(trade);
		}

		try (FileWriter fw = new FileWriter("c:/tmp/saccr.csv")) {

			fw.write(Trade.getHeadersString());
			fw.write("\n");
			for (String str : tradeList) {
				fw.write(str);
				fw.write("\n");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return results;
	}

	private Trade buildTrade(com.quark.risk.saccr.model.Trade t) throws ApplicationException {
		if (!reqTradeAttrMap.containsKey(t.getProductName())) {
			results.addMessage(String.format("Un-recognized product name : %s", t.getProductName()));
			return null;
		}
		Trade trade = new Trade();
		try {
			PropertyUtils.copyProperties(trade, t);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		}
		trade.setAssetClass(AssetClass.valueOf(reqTradeAttrMap.get(t.getProductName()).getAssetClass()));
		setMPOR(trade);
		setCollateralDetails(trade);
		setStartDate(trade);
		setEndDate(trade);
		setMaturityTenor(trade);
		setNextExecTenor(trade);
		setSupervisoryDuration(trade);
		setSupervisoryFactor(trade);
		setSupervisoryDelta(trade);
		setMaturityFactor(trade);
		setMaturityFactorUn(trade);
		setAdjustedNotional(trade, false);
		setAdjustedNotional(trade, true);
		setTradeEffectiveNotional(trade, false);
		setTradeEffectiveNotional(trade, true);
		setReportingNotional(trade);
		setAddOn(trade, false);
		setAddOn(trade, true);
		setReplacementCost(trade);
		setMultiplier(trade, false);
		setMultiplier(trade, true);
		setPfe(trade, false);
		setPfe(trade, true);
		setEAD(trade, false);
		setEAD(trade, true);
		return trade;
	}

	private void setEAD(Trade trade, boolean unmargined) {
		if (unmargined) {
			trade.setEadUn(alpha * (trade.getReplacementCost() + trade.getPfeUn()));
		} else {
			trade.setEad(alpha * (trade.getReplacementCost() + trade.getPfe()));
		}
	}

	private void setPfe(Trade trade, boolean unmargined) {
		if (unmargined) {
			trade.setPfe(trade.getAddOn() * trade.getMultiplier());
		} else {
			trade.setPfeUn(trade.getAddOnUn() * trade.getMultiplierUn());
		}
	}

	private void setMultiplier(Trade trade, boolean unmargined) {
		// double rc = trade.getReplacementCost();
		double rc = trade.getNpv() * getRate(trade.getNpvCCY())
				- trade.getCollateralBalance() * (1 - trade.getHaircut());

		double addOn = unmargined ? trade.getAddOnUn() : trade.getAddOn();
		double multiplier;
		if (Double.compare(addOn, 0.0) == 0) {
			multiplier = 1;
		} else {
			double d = rc / (2 * 0.95 * addOn);
			if (d > 0) {
				multiplier = 1;
			} else {
				multiplier = 0.05 + 0.95 * Math.exp(d);
			}
		}
		if (unmargined) {
			trade.setMultiplierUn(multiplier);
		} else {
			trade.setMultiplier(multiplier);
		}
	}

	private void setReplacementCost(Trade trade) {
		double npxFx = getRate(trade.getNpvCCY());
		double rc = Math.max(trade.getNpv() * npxFx - trade.getCollateralBalance() * (1 - trade.getHaircut()), 0);
		if (StringUtils.isNotBlank(trade.getCsaName())) {
			trade.setReplacementCost(Math.max(rc, trade.getMta() + trade.getThresholdAmount() - trade.getNica()));
		}
		trade.setReplacementCost(rc);
	}

	private void setAddOn(Trade trade, boolean unmargined) {
		double addOn = trade.getSupervisoryFactor()
				* Math.abs(unmargined ? trade.getEffectiveNotionalUn() : trade.getEffectiveNotional());
		if (ProductNames.IR_SWAP_BASIS.equalsIgnoreCase(trade.getProductName())) {
			addOn *= 0.5;
		}
		if (unmargined) {
			trade.setAddOnUn(addOn);
		} else {
			trade.setAddOn(addOn);
		}
	}

	private void setTradeEffectiveNotional(Trade trade, boolean unmargined) {
		double effectiveNotional;
		if (ProductNames.EQUITY_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.FX_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.IR_OPTION_CAP_DIGITAL.equalsIgnoreCase(trade.getProductName())) {
			effectiveNotional = getPutCallFlag(trade) * getBuySellFlag(trade) * Math.abs(trade.getRegulatoryDelta())
					* (trade.getPayOffAmount() * getRate(trade.getPayoffCcy())
							- Math.abs(trade.getNpv() * getRate(trade.getNpvCCY())))
					/ trade.getSupervisoryFactor();
		} else {
			if (ProductNames.FX_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.FX_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())) {
				effectiveNotional = getDeltaSign(trade) * getBuySellFlag(trade)
						* (trade.getPayOffAmount() * getRate(trade.getPayoffCcy())
								- Math.abs(trade.getNpv() * getRate(trade.getNpvCCY())))
						/ trade.getSupervisoryFactor();
			} else {
				if (unmargined) {
					effectiveNotional = trade.getAdjustedNotionalUn() * trade.getMaturityFactorUn()
							* trade.getRegulatoryDelta();
				} else {
					effectiveNotional = trade.getAdjustedNotional() * trade.getMaturityFactor()
							* trade.getRegulatoryDelta();
				}
			}
		}
		if (unmargined) {
			trade.setEffectiveNotionalUn(effectiveNotional);
		} else {
			trade.setEffectiveNotional(effectiveNotional);
		}
	}

	private int getDeltaSign(Trade trade) {
		int sign = -1;
		if (trade.getUnderlyingPrice() >= Math.sqrt(trade.getStrike() * trade.getStrike2())) {
			if (ProductNames.FX_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())) {
				sign = 1;
			}
		} else {
			if (ProductNames.FX_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())) {
				sign = 1;
			}
		}
		return sign;
	}

	private void setReportingNotional(Trade trade) {
		double reportingNotional;
		if (ProductNames.EQUITY_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.FX_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.IR_OPTION_CAP_DIGITAL.equalsIgnoreCase(trade.getProductName())) {
			reportingNotional = Math.abs(trade.getEffectiveNotional()
					/ (trade.getMaturityFactor() * trade.getSupervisoryDuration() * trade.getRegulatoryDelta()));
		} else {
			if (ProductNames.FX_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.FX_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())) {
				reportingNotional = Math.abs(
						trade.getEffectiveNotional() / (trade.getMaturityFactor() * trade.getSupervisoryDuration()));
			} else {
				reportingNotional = Math.abs(trade.getEffectiveNotional() / trade.getSupervisoryDuration());
			}
		}
		trade.setReportingNotional(reportingNotional);
	}

	private void setAdjustedNotional(Trade trade, boolean unmargined) {
		double adjustedNotional = 0;
		if (AssetClass.IR.equals(trade.getAssetClass()) || AssetClass.CREDIT.equals(trade.getAssetClass())) {
			adjustedNotional = trade.getNotional() * getRate(trade.getNotionalCcy()) * trade.getSupervisoryDuration();
			if (ProductNames.IR_OPTION_CAP_DIGITAL.equalsIgnoreCase(trade.getProductName())) {
				adjustedNotional = calcDigitalOptionNotional(trade, unmargined, getRate(trade.getNpvCCY()));
			}
		} else if (AssetClass.FX.equals(trade.getAssetClass())) {
			if (ProductNames.FX_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())) {
				adjustedNotional = calcDigitalOptionNotional(trade, unmargined, getRate(trade.getNpvCCY()));
			} else {
				if (baseCurrency.equalsIgnoreCase(trade.getPayCCY())) {
					adjustedNotional = trade.getRecNotional() * getRate(trade.getRecCCY());
				} else if (baseCurrency.equalsIgnoreCase(trade.getRecCCY())) {
					adjustedNotional = trade.getPayNotional() * getRate(trade.getPayCCY());
				} else {
					adjustedNotional = Math.max(trade.getPayNotional() * getRate(trade.getPayCCY()),
							trade.getRecNotional() * getRate(trade.getRecCCY()));
				}
			}
		} else if (AssetClass.EQ.equals(trade.getAssetClass()) || AssetClass.CMDTY.equals(trade.getAssetClass())) {
			if (Double.compare(trade.getNotional(), 0) != 0) {
				adjustedNotional = trade.getNotional() * getRate(trade.getNotionalCcy());
			} else {
				adjustedNotional = trade.getQuantity() * trade.getUnderlyingPrice() * trade.getTickSize()
						* getRate(trade.getNotionalCcy());
			}
			if (ProductNames.EQUITY_OPTION_QUANTO.equalsIgnoreCase(trade.getProductName())) {
				adjustedNotional = adjustedNotional * trade.getQuantoFXRate();
			} else if (ProductNames.EQUITY_OPTION_DIGITAL.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DNT.equalsIgnoreCase(trade.getProductName())
					|| ProductNames.EQUITY_OPTION_DIGITAL_DOT.equalsIgnoreCase(trade.getProductName())) {
				adjustedNotional = calcDigitalOptionNotional(trade, unmargined, getRate(trade.getNpvCCY()));
			}
		}
		if (unmargined) {
			trade.setAdjustedNotionalUn(adjustedNotional);
		} else {
			trade.setAdjustedNotional(adjustedNotional);
		}
	}

	private double calcDigitalOptionNotional(Trade trade, boolean unmargined, double npvFX) {
		int putCall = getPutCallFlag(trade);
		int buySell = getBuySellFlag(trade);
		double digitalFX = getRate(trade.getPayoffCcy());
		double mf = trade.getMaturityFactor();
		if (unmargined) {
			mf = trade.getMaturityFactorUn();
		}
		return putCall * buySell * (trade.getPayOffAmount() * digitalFX - Math.abs(trade.getNpv() * npvFX))
				/ (trade.getSupervisoryFactor() * mf);
	}

	private void setMaturityFactorUn(Trade trade) {
		trade.setMaturityFactorUn(Math.sqrt(Math.min(trade.getMaturityTenor(), 1)));
	}

	private void setMaturityFactor(Trade trade) {
		double matFactor;
		if (StringUtils.isNotBlank(trade.getCsaName())) {
			matFactor = 1.5 * Math.sqrt(trade.getMpor() / businessDaysInAYear);
		} else {
			matFactor = Math.sqrt(Math.min(trade.getMaturityTenor(), 1));
		}
		trade.setMaturityFactor(matFactor);
	}

	private void setSupervisoryDelta(Trade trade) throws ApplicationException {
		double vol = deriveSupervisoryVol(trade);
		if (Double.compare(vol, 0) == 0) {
			throw new ApplicationException(-1, "Failed to derive supervisory vol");
		}
		int position = BuySell.BUY.equals(trade.getBuySell()) ? 1 : -1;
		double regDelta;
		if (trade.getPutCall() != null && trade.getNextExecTenor() > 0) {
			double d1;
			if (trade.getUnderlyingPrice() > 0 && trade.getStrike() > 0) {
				d1 = (Math.log(trade.getUnderlyingPrice() / trade.getStrike())
						+ 0.5 * vol * vol * trade.getNextExecTenor()) / (vol * Math.sqrt(trade.getNextExecTenor()));
			} else {
				d1 = (trade.getUnderlyingPrice() - trade.getStrike()) / (vol * Math.sqrt(trade.getNextExecTenor()));
			}
			if (PutCall.CALL.equals(trade.getPutCall())) {
				regDelta = position * normsdist.cumulativeProbability(d1);
			} else {
				regDelta = -1 * position * normsdist.cumulativeProbability(-1 * d1);
			}
		} else {
			regDelta = position;
		}
		trade.setRegulatoryDelta(regDelta);
	}

	private void setSupervisoryFactor(Trade trade) throws ApplicationException {
		double supFactor = deriveSupervisoryFactor(trade);
		if (Double.compare(supFactor, 0) == 0.0) {
			deriveSupervisoryFactor(trade);
			throw new ApplicationException(-1, "Failed to derive supervisory factor");
		}
		if (ProductNames.IR_SWAP_BASIS.equals(trade.getProductName())) {
			supFactor *= 0.5;
		}
		trade.setSupervisoryFactor(supFactor);
	}

	private double deriveSupervisoryFactor(Trade trade) {
		double supFactor = 0;
		for (SupervisoryParameters p : supervisoryParameters) {
			if (p.getAssetClass().equals(trade.getAssetClass()) && compareSubClass(trade, p)) {
				supFactor = p.getSupervisoryFactor();
				break;
			}
		}
		return supFactor;
	}

	private double deriveSupervisoryVol(Trade trade) {
		double supFactor = 0;
		for (SupervisoryParameters p : supervisoryParameters) {
			if (p.getAssetClass().equals(trade.getAssetClass()) && compareSubClass(trade, p)) {
				supFactor = p.getSupervisoryOptionVol();
				break;
			}
		}
		return supFactor;
	}

	private boolean compareSubClass(Trade trade, SupervisoryParameters p) {
		if (StringUtils.isBlank(trade.getSubCls()) && StringUtils.isBlank(p.getSubClass())) {
			return true;
		}
		if (StringUtils.isNotBlank(trade.getSubCls())) {
            return (StringUtils.isNotBlank(p.getSubClass())
                    && trade.getSubCls().toUpperCase().equalsIgnoreCase(p.getSubClass().toUpperCase()))
                    || trade.getSubCls().equalsIgnoreCase(p.getSingleNameOrIndex().toString());
        } else
            return StringUtils.isBlank(trade.getSubCls()) && p.getSingleNameOrIndex() == SingleNameOrIndex.NA;
    }

	private void setSupervisoryDuration(Trade trade) {
		double superDur = 1;
		if (AssetClass.IR.equals(trade.getAssetClass()) || AssetClass.CREDIT.equals(trade.getAssetClass())) {
			superDur = (Math.exp(-0.05 * trade.getStartTenor())
					- Math.exp(-0.05 * Math.max(10.0d / businessDaysInAYear, trade.getEndTenor()))) / 0.05;
		}
		trade.setSupervisoryDuration(superDur);
	}

	private void setNextExecTenor(Trade trade) {
		if (trade.getNextExerciseDate() != null) {
			trade.setNextExecTenor(getDifferenceDays(valueDate, trade.getNextExerciseDate()) / daysInAYear);
		}
	}

	/**
	 * IR_BOND_FUTURES_OPTION has two cases
	 * 
	 * @param trade
	 */
	private void setMaturityTenor(Trade trade) {
		Date endDate = trade.getEarlyTerminationDate() != null ? trade.getEarlyTerminationDate() : trade.getEndDate();
		double maturityTenor = Math.max(10.0d / businessDaysInAYear,
				getDifferenceDays(valueDate, endDate) / daysInAYear);
		if (ProductNames.IR_BOND_FUTURES_OPTION.equalsIgnoreCase(trade.getProductName())) {
			if (SettleType.CASH.toString().equalsIgnoreCase(trade.getSettlementType())
					|| StringUtils.isBlank(trade.getSettlementType())) {
				maturityTenor = Math.max(10.0d / businessDaysInAYear,
						getDifferenceDays(valueDate, trade.getNextExerciseDate()) / daysInAYear);
			} else {
				maturityTenor = Math.max(10.0d / businessDaysInAYear,
						getDifferenceDays(trade.getStartDate(), trade.getNextExerciseDate()) / daysInAYear);
			}
		} else if (ProductNames.IR_SWAPTION_EUROPEAN.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.IR_SWAPTION_BERMUDAN.equalsIgnoreCase(trade.getProductName())) {
			if (SettleType.CASH.toString().equalsIgnoreCase(trade.getSettlementType())
					|| StringUtils.isBlank(trade.getSettlementType())) {
				maturityTenor = Math.max(10.0d / businessDaysInAYear,
						getDifferenceDays(valueDate, trade.getNextExerciseDate()) / daysInAYear);
			}
		} else if (ProductNames.IR_BOND_FUTURES.equalsIgnoreCase(trade.getProductName())) {
			maturityTenor = Math.max(10.0d / businessDaysInAYear,
					getDifferenceDays(valueDate, trade.getStartDate()) / daysInAYear);
		} else if ((ProductNames.IR_FUTURES_SWAP.equalsIgnoreCase(trade.getProductName())
				|| ProductNames.IR_FUTURES_EURODOLLAR.equalsIgnoreCase(trade.getProductName()))
				&& (SettleType.CASH.toString().equalsIgnoreCase(trade.getSettlementType())
						|| StringUtils.isBlank(trade.getSettlementType()))) {
			maturityTenor = Math.max(10.0d / businessDaysInAYear,
					getDifferenceDays(valueDate, trade.getStartDate()) / daysInAYear);
		}
		trade.setMaturityTenor(maturityTenor);
	}

	private void setEndDate(Trade trade) {
		trade.setEndTenor(getDifferenceDays(valueDate,
				trade.getEarlyTerminationDate() != null ? trade.getEarlyTerminationDate() : trade.getEndDate())
				/ daysInAYear);
	}

	private void setStartDate(Trade trade) {
		trade.setStartTenor(Math.max(0, getDifferenceDays(valueDate, trade.getStartDate()) / daysInAYear));
	}

	private double getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	private void setMPOR(Trade t) {
		Collateral c = collateralMap.get(new CollateralLookupKey(t.getLaName(), t.getCsaName()));
		int collateralFreq = 1;
		boolean dispute = false;
		boolean liquid = false;
		if (c != null) {
			collateralFreq = c.getCollateralCallFrequency();
			dispute = c.getDispute();
			liquid = c.getDispute();
		}
		int mpor;
		if (ContractType.BROKER_CLIENT.toString().equalsIgnoreCase(t.getContractType())) {
			mpor = mporBrokerClientInitValue + collateralFreq - 1;
		} else {
			mpor = mporInitValue + collateralFreq - 1;
		}
		if (dispute && !liquid) {
			mpor *= 2;
		}
		t.setMpor(mpor);
	}

	private void setCollateralDetails(Trade t) {
		Collateral c = collateralMap.get(new CollateralLookupKey(t.getLaName(), t.getCsaName()));
		if (c != null) {
			t.setCollateralBalance(c.getCollateralBalance());
			t.setHaircut(c.getHaircut());
			t.setThresholdAmount(c.getThresholdForPo());
			t.setMta(c.getMtaForPo());
			t.setNica(c.getNica());
		}
	}

	private double getRate(String ccy) {
		if (baseCurrency.equalsIgnoreCase(ccy)) {
			return 1;
		}
		String key = String.format("FX.%s.%s", ccy, baseCurrency);
		SaccrMarketQuote q = marketData.get(key);
		if (q != null) {
			return q.getQuoteValue();
		} else {
			log.error(String.format("FX rate not found for quote '%s'", key));
			return 1;
		}
	}

	private boolean validate() {
		if (valueDate == null) {
			results.addMessage("Value date is not set");
		}
		if (StringUtils.isBlank(baseCurrency)) {
			results.addMessage("Base currency is not set");
		}
		if (marketData.isEmpty()) {
			results.addMessage("Market data is empty");
		}
		if (trades.isEmpty()) {
			results.addMessage("Trade data is empty");
		}
		return !results.hasMessages();
	}

	public void setReqTradeAttributes(List<RequiredTradeAttributes> reqTradeAttributes) {
		reqTradeAttrMap = new HashMap<>();
		for (RequiredTradeAttributes attr : reqTradeAttributes) {
			reqTradeAttrMap.put(attr.getProductName(), attr);
		}
	}

	public void setCollateral(List<Collateral> collateral) {
		collateralMap = new HashMap<>();
		for (Collateral c : collateral) {
			collateralMap.put(new CollateralLookupKey(c.getLaId(), c.getCsaId()), c);
		}
	}

	public void setMarketData(List<SaccrMarketQuote> quotes) {
		marketData = new HashMap<>();
		for (SaccrMarketQuote q : quotes) {
			marketData.put(q.getQuoteName(), q);
		}
	}

	private int getPutCallFlag(Trade trade) {
		if (PutCall.CALL.equals(trade.getPutCall())) {
			return 1;
		} else {
			return -1;
		}
	}

	private int getBuySellFlag(Trade trade) {
		if (BuySell.BUY.equals(trade.getBuySell())) {
			return 1;
		} else {
			return -1;
		}
	}
}
