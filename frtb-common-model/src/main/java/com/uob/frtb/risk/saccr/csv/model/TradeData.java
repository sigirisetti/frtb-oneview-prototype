package com.uob.frtb.risk.saccr.csv.model;

import com.uob.frtb.risk.saccr.model.BuySell;
import com.uob.frtb.risk.saccr.model.ContractType;
import com.uob.frtb.risk.saccr.model.PutCall;
import com.uob.frtb.risk.saccr.model.RequiredTradeAttributes;
import com.uob.frtb.risk.saccr.model.Trade;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeData extends SACCRData<Trade> {

	private static final long serialVersionUID = -9178750405247977004L;

	private NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.US);

	private String productName;
	private String contractType;
	private String legalEntity;
	private String laName;
	private String csaName;
	private String po;
	private String tradeId;
	private String sourceSystem;
	private String npvCCY;
	private String npv;
	private String startDate;
	private String endDate;
	private String nextExerciseDate;
	private String buySell;
	private String putCall;
	private String payRateIndex;
	private String recRateIndex;
	private String payoffCcy;
	private String payOffAmount;
	private String notionalCcy;
	private String notional;
	private String quantity;
	private String tickSize;
	private String underlyingPrice;
	private String strike;
	private String quantoFXRate;
	private String payCCY;
	private String recCCY;
	private String payNotional;
	private String recNotional;
	private String subCls;
	private String underlyingProduct;
	private String settlementType;
	private String earlyTerminationDate;
	private String optionType;
	private String strike2;
	private String exerciseType;

	private Map<String, RequiredTradeAttributes> config;

	public Trade build() {
		Trade t = new Trade();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(productName)) {
			t.setProductName(productName);
		} else {
			addMessage("Product Name is missing");
		}
		RequiredTradeAttributes reqAttrs = config.get(productName);
		if (reqAttrs == null) {
			addMessage("Required attributes config is missing");
			return null;
		}
		if (StringUtils.isNotBlank(contractType)) {
			t.setContractType(contractType);
		} else {
			addMessage("Contract type is missing");
		}
		if (StringUtils.isNotBlank(legalEntity)) {
			t.setLegalEntity(legalEntity);
		} else {
			addMessage("Legal Entity is missing");
		}
		if (StringUtils.isNotBlank(laName)) {
			t.setLaName(laName);
		} else if (ContractType.OTC.toString().equals(contractType) && StringUtils.isEmpty(csaName)) {
			addMessage("LA Name is missing");
		}
		if (StringUtils.isNotBlank(csaName)) {
			t.setLaName(csaName);
		} else if (ContractType.OTC.toString().equals(contractType) && StringUtils.isEmpty(laName)) {
			addMessage("LA Name and CSA Name is missing");
		}
		if (StringUtils.isNotBlank(po)) {
			t.setPo(po);
		} else {
			addMessage("Processing Org is missing");
		}
		if (StringUtils.isNotBlank(tradeId)) {
			t.setTradeId(tradeId);
		} else {
			addMessage("Trade Id is missing");
		}
		if (StringUtils.isNotBlank(sourceSystem)) {
			t.setSourceSystem(sourceSystem);
		} else {
			addMessage("Source system is missing");
		}
		if (StringUtils.isNotBlank(npvCCY)) {
			t.setNpvCCY(npvCCY);
		} else {
			addMessage("NPV currency is missing");
		}
		try {
			t.setNpv(getDouble(npv));
		} catch (ParseException e) {
			if (reqAttrs.isNpv()) {
				addMessage("NPV is not a number");
			}
		}
		try {
			t.setStartDate(sdf.parse(startDate));
		} catch (ParseException e) {
			addMessage("Start date format is wrong");
		}
		try {
			t.setEndDate(sdf.parse(endDate));
		} catch (ParseException e) {
			addMessage("End date format is wrong");
		}
		if (StringUtils.isNotBlank(nextExerciseDate)) {
			try {
				t.setNextExerciseDate(sdf.parse(nextExerciseDate));
			} catch (ParseException e) {
				if (reqAttrs.isNextExerciseDate()) {
					addMessage("Next exercise date format is wrong");
				}
			}
		}
		try {
			t.setBuySell(BuySell.valueOf(buySell));
		} catch (Exception e) {
			addMessage("Invalid Buy Sell flag");
		}
		try {
			t.setPutCall(PutCall.valueOf(putCall));
		} catch (Exception e) {
			if (reqAttrs.isPutCall()) {
				addMessage("Invalid Put Call flag");
			}
		}
		if (StringUtils.isNotBlank(payRateIndex)) {
			t.setPayRateIndex(payRateIndex);
		} else if (reqAttrs.isPayRefIndex()) {
			addMessage("Pay rate index is missing");
		}
		if (StringUtils.isNotBlank(recRateIndex)) {
			t.setRecRateIndex(recRateIndex);
		} else if (reqAttrs.isRecRefIndex()) {
			addMessage("Receive rate index is missing");
		}
		if (StringUtils.isNotBlank(payoffCcy)) {
			t.setPayoffCcy(payoffCcy);
		} else if (reqAttrs.isPayoffCcy()) {
			addMessage("Payoff currency is missing");
		}
		try {
			t.setPayOffAmount(getDouble(payOffAmount));
		} catch (ParseException e) {
			if (reqAttrs.isPayOffAmount()) {
				addMessage("Payoff amount is missing");
			}
		}
		if (StringUtils.isNotBlank(notionalCcy)) {
			t.setNotionalCcy(notionalCcy);
		} else if (reqAttrs.isNotional()) {
			addMessage("Notional currency is missing");
		}
		try {
			t.setNotional(getDouble(notional));
		} catch (ParseException e) {
			if (reqAttrs.isNotional()) {
				addMessage("Notional is missing");
			}
		}
		try {
			t.setQuantity(getDouble(quantity));
		} catch (ParseException e) {
			if (reqAttrs.isQuantity()) {
				addMessage("Quantity is missing");
			}
		}
		try {
			t.setTickSize(getInt(tickSize));
		} catch (ParseException e) {
			if (reqAttrs.isTickSize()) {
				addMessage("Tick size is missing");
			}
		}
		try {
			t.setUnderlyingPrice(getDouble(underlyingPrice));
		} catch (ParseException e) {
			if (reqAttrs.isUnderlyingPrice()) {
				addMessage("Strike price is missing");
			}
		}
		try {
			t.setStrike(getDouble(strike));
		} catch (ParseException e) {
			if (reqAttrs.isStrike()) {
				addMessage("Strike price is missing");
			}
		}
		try {
			t.setQuantoFXRate(getDouble(quantoFXRate));
		} catch (ParseException e) {
			if (reqAttrs.isQuantoFXRate()) {
				addMessage("Quanto FX Rate is missing");
			}
		}
		if (StringUtils.isNotBlank(payCCY)) {
			t.setPayCCY(payCCY);
		} else {
			if (reqAttrs.isPayCcy()) {
				addMessage("Pay Currency is missing");
			}
		}
		if (StringUtils.isNotBlank(recCCY)) {
			t.setRecCCY(recCCY);
		} else {
			if (reqAttrs.isRecCcy()) {
				addMessage("Receive Currency is missing");
			}
		}
		try {
			t.setPayNotional(getDouble(payNotional));
		} catch (ParseException e) {
			if (reqAttrs.isPayNotional()) {
				addMessage("Pay notional is missing");
			}
		}
		try {
			t.setRecNotional(getDouble(recNotional));
		} catch (ParseException e) {
			if (reqAttrs.isRecNotional()) {
				addMessage("Receive notional is missing");
			}
		}
		if (StringUtils.isNotBlank(subCls)) {
			t.setSubCls(subCls);
		} else if (reqAttrs.isSubCls()) {
			addMessage("Sub Class is missing");
		}
		if (StringUtils.isNotBlank(underlyingProduct)) {
			t.setUnderlyingProduct(underlyingProduct);
		} else if (reqAttrs.isUnderlyingProduct()) {
			addMessage("Underlying product is missing");
		}
		if (StringUtils.isNotBlank(settlementType)) {
			t.setSettlementType(settlementType);
		} else if (reqAttrs.isSettlementType()) {
			addMessage("Settlement type is missing");
		}
		if (StringUtils.isNotBlank(earlyTerminationDate)) {
			try {
				t.setEarlyTerminationDate(sdf.parse(earlyTerminationDate));
			} catch (ParseException e) {
				if (reqAttrs.isEarlyTerminationDate()) {
					addMessage("Early termination date format is wrong");
				}
			}
		}
		if (StringUtils.isNotBlank(optionType)) {
			t.setOptionType(optionType);
		} else if (reqAttrs.isOptionType()) {
			addMessage("Option type is missing");
		}
		try {
			t.setStrike2(getDouble(strike2));
		} catch (ParseException e) {
			if (reqAttrs.isStrike2()) {
				addMessage("Strike Price 2 is missing");
			}
		}
		return t;
	}

	public double getDouble(String str) throws ParseException {
		return nf.parse(str).doubleValue();
	}

	public int getInt(String str) throws ParseException {
		return nf.parse(str).intValue();
	}

	public String toCsv() {
		return StringUtils.join(new String[] { productName, contractType, legalEntity, laName, csaName, po, tradeId,
				sourceSystem, npvCCY, npv, startDate, endDate, nextExerciseDate, buySell, putCall, payRateIndex,
				recRateIndex, payoffCcy, payOffAmount, notionalCcy, notional, quantity, tickSize, underlyingPrice,
				strike, quantoFXRate, payCCY, recCCY, payNotional, recNotional, subCls, underlyingProduct,
				settlementType, earlyTerminationDate, optionType, strike2 }, ",");
	}
}
