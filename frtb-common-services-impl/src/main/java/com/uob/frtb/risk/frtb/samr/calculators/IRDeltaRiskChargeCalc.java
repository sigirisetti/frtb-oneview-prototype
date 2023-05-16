package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.refdata.model.Tenor;
import com.uob.frtb.risk.frtb.samr.config.IRDeltaConfig;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.RiskWeight;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.IRDeltaTradeLevelResults;
import com.uob.frtb.risk.frtb.samr.results.IntermediateResultEntity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * IR delta margin.
 * 
 * Computes risk charge and simple sums at Rate Index level, Currency Bucket
 * level and IR level
 * 
 * @author Surya
 */
@Slf4j
@Component
@Scope("prototype")
public class IRDeltaRiskChargeCalc extends BaseRiskChargeCalculator {

	private Map<String, RateIndexCollection> currRateIndexCol = new TreeMap<>();

	@Autowired
	private IRDeltaConfig irCorrConfig;

	@Override
	public void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.IR_DELTA == sens.getSensitivityType()) {
				Delta d = Delta.fromTradeSensitivity(sens);
				RateIndexCollection riSet = currRateIndexCol.get(d.getCurrency());
				if (riSet == null) {
					riSet = new RateIndexCollection(d.getCurrency());
					currRateIndexCol.put(d.getCurrency(), riSet);
				}
				riSet.add(d);
			}
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (currRateIndexCol.isEmpty()) {
			return;
		}
		debugRateIndexData();

		List<RiskWeight> defaultRiskWeights = irCorrConfig.getDefaultRiskWeights();
		List<Double> kb = new ArrayList<>();
		List<Double> sb = new ArrayList<>();
		for (RateIndexCollection riCol : currRateIndexCol.values()) {
			riCol.calculateMargin(defaultRiskWeights);
			kb.add(riCol.getKbBase());
			sb.add(Math.max(Math.min(riCol.getKbBase(), riCol.getSbBase()), -riCol.getKbBase()));
		}
		riskCharge = calculateRiskCharge(irCorrConfig.getIrCrossBucketCorr(), kb, sb);
		log.info("IR Delta Margin is {} {}", riskCharge, calcRequest.getBaseCurrency());
	}

	private void debugRateIndexData() {
		for (RateIndexCollection riCol : currRateIndexCol.values()) {
			for (RateIndex ri : riCol.getRateIdxMap().values()) {
				log.debug(ri.getCurrency() + ", " + ri.getIndex() + ", " + ri.getDeltaMap());
			}
		}
	}

	@Getter
	private class RateIndexCollection extends Sum {
		private String currency;
		private Map<String, RateIndex> rateIdxMap = new TreeMap<>();

		public RateIndexCollection(String currency) {
			this.currency = currency;
		}

		void calculateMargin(List<RiskWeight> riskWeights) {
			int r = 0;
			weightedSensitivities = new Array2DRowRealMatrix(rateIdxMap.size() * riskWeights.size(), 1);
			for (RateIndex ri : rateIdxMap.values()) {
				ri.calculateMargin(riskWeights);
				RealMatrix wk = ri.getWeightedSensitivities();
				for (int i = 0; i < wk.getRowDimension(); i++) {
					RiskWeight w = riskWeights.get(i);
					weightedSensitivities.setEntry(r++, 0, wk.getEntry(i, 0) * w.getWeight() * 10000);
					sb += wk.getEntry(i, 0);
				}
			}
			kb = weightedSensitivities.transpose().multiply(irCorrConfig.ratesCorrelation(rateIdxMap.size()))
					.multiply(weightedSensitivities).getEntry(0, 0);
			kb = Math.sqrt(Math.max(kb, 0));
			double fx = getRate(currency);
			kbBase = kb * fx;
			sbBase = sb * fx;
		}

		void add(Delta d) {
			RateIndex idx = rateIdxMap.get(d.getIndex());
			if (idx == null) {
				idx = new RateIndex(d.getCurrency(), d.getIndex());
				rateIdxMap.put(d.getIndex(), idx);
			}
			idx.add(d);
		}
	}

	@Getter
	@Setter
	private class RateIndex extends Sum {
		private String currency;
		private String index;
		private Map<Integer, Delta> deltaMap = new TreeMap<>();
		private Map<String, Trade> trades = new HashMap<>();

		RateIndex(String currency, String index) {
			this.currency = currency;
			this.index = index;
		}

		void calculateMargin(List<RiskWeight> riskWeights) {
			calculateSums(currency, this, riskWeights, deltaMap);
			for (Map.Entry<String, Trade> me : trades.entrySet()) {
				me.getValue().calculateMargin(riskWeights);
			}
		}

		void add(Delta d) {
			Delta d0 = deltaMap.get(d.getTenorCode());
			if (d0 == null) {
				deltaMap.put(d.getTenorCode(), d);
			} else {
				d0.add(d);
			}
			Trade t = trades.get(d.getIdentifier());
			if (t == null) {
				t = new Trade(d.getIdentifier(), d.getCurrency());
				trades.put(d.getIdentifier(), t);
			}
			t.add(d.clone());
		}
	}

	private void calculateSums(String currency, Sum ref, List<RiskWeight> riskWeights, Map<Integer, Delta> deltaMap) {
		RealMatrix weightedSensitivities = new Array2DRowRealMatrix(riskWeights.size(), 1);
		for (int i = 0; i < riskWeights.size(); i++) {
			RiskWeight w = riskWeights.get(i);
			Delta d = deltaMap.get(w.getTenorCode());
			if (d != null) {
				weightedSensitivities.setEntry(i, 0, d.getAmount());
				ref.sb += d.getAmount();
			} else {
				weightedSensitivities.setEntry(i, 0, 0);
				log.error("Delta not found for tenor : " + new Tenor(w.getTenorCode(), Tenor.TimeUnit.D).toString());
			}
		}
		double val = weightedSensitivities.transpose().multiply(irCorrConfig.ratesCorrelation())
				.multiply(weightedSensitivities).getEntry(0, 0);
		ref.setWeightedSensitivities(weightedSensitivities);
		ref.kb = Math.sqrt(Math.max(val, 0));
		double fx = getRate(currency);
		ref.kbBase = ref.kb * fx;
		ref.sbBase = ref.sb * fx;
	}

	@Getter
	@Setter
	private class Trade extends Sum {
		private String identifier;
		private String currency;

		Trade(String identifier, String currency) {
			this.identifier = identifier;
			this.currency = currency;
		}

		private Map<Integer, Delta> deltaMap = new TreeMap<>();

		void calculateMargin(List<RiskWeight> riskWeights) {
			calculateSums(currency, this, riskWeights, deltaMap);
			weightedSensitivities = null;
		}

		public void add(Delta d) {
			deltaMap.put(d.getTenorCode(), d);
		}
	}

	@Getter
	@AllArgsConstructor
	private static class Delta implements Cloneable {
		private String identifier;
		private String currency;
		private String index;
		private String tenor;
		private int tenorCode;
		private double amount;

		void add(Delta d) {
			this.amount += d.getAmount();
		}

		static Delta fromTradeSensitivity(TradeSensitivity tradeSens) {
			return new Delta(tradeSens.getTradeIdentifier(), tradeSens.getCurrency(), tradeSens.getIndex(),
					tradeSens.getTenor(), tradeSens.getTenorCode(), tradeSens.getSensitivity());
		}

		@Override
		public String toString() {
			return String.valueOf(amount);
		}

		public Delta clone() {
			return new Delta(identifier, currency, index, tenor, tenorCode, amount);
		}
	}

	@Getter
	@Setter
	private class Sum {
		protected RealMatrix weightedSensitivities;
		protected double kb;
		protected double kbBase;
		protected double sb;
		protected double sbBase;
	}

	@Override
	void saveResults(PoResults results) {
		log.info("IR Delta - Saving sensitivity results");
		if (currRateIndexCol.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DELTA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
		List<IntermediateResultEntity> interResults = new ArrayList<>();
		for (RateIndexCollection riCol : currRateIndexCol.values()) {
			log.debug("Saving results for currency : {}", riCol.getCurrency());

			RiskClassHierarchyResultRow irCurrResult = new RiskClassHierarchyResultRow(results, riCol.getCurrency(),
					results.getSamrResults().getCurrency(), riCol.getCurrency(), deltaResult.buildParentKey(),
					riCol.getKb(), riCol.getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(irCurrResult);
			for (RateIndex ri : riCol.getRateIdxMap().values()) {
				log.debug("Saving results for currency : {} and rate index : {}", riCol.getCurrency(), ri.getIndex());
				RiskClassHierarchyResultRow irRateIdxResult = new RiskClassHierarchyResultRow(results,
						riCol.getCurrency(), results.getSamrResults().getCurrency(), ri.getIndex(),
						irCurrResult.buildParentKey(), ri.getKb(), ri.getKbBase(), 0, 0, 0, 0);
				results.addHierarchyResultRow(irRateIdxResult);
				for (Map.Entry<String, Trade> me : ri.getTrades().entrySet()) {
					interResults.add(buildInterResult(results, me.getValue()));
				}
			}
		}
		log.info("Total number of trade results : {}", interResults.size());
		results.setInterResults(interResults);
		/*
		if (calcRequest.isPersistResults()) {
			coreDao.save(interResults);
		}
		*/
	}

	IntermediateResultEntity buildInterResult(PoResults results, Trade trade) {
		IRDeltaTradeLevelResults t = new IRDeltaTradeLevelResults();
		t.setTradeIdentifier(trade.getIdentifier());
		String index = null;
		for (Map.Entry<Integer, Delta> me : trade.getDeltaMap().entrySet()) {
			Delta d = me.getValue();
			t.setKb(trade.getKb());
			t.setKbBase(trade.getKbBase());
			t.setSb(trade.getSb());
			t.setSbBase(trade.getSbBase());
			index = d.getIndex();
			if ("3M".equalsIgnoreCase(d.getTenor())) {
				t.set_3M(d.getAmount());
			}
			if ("6M".equalsIgnoreCase(d.getTenor())) {
				t.set_6M(d.getAmount());
			}
			if ("1Y".equalsIgnoreCase(d.getTenor())) {
				t.set_1Y(d.getAmount());
			}
			if ("2Y".equalsIgnoreCase(d.getTenor())) {
				t.set_2Y(d.getAmount());
			}
			if ("3Y".equalsIgnoreCase(d.getTenor())) {
				t.set_3Y(d.getAmount());
			}
			if ("5Y".equalsIgnoreCase(d.getTenor())) {
				t.set_5Y(d.getAmount());
			}
			if ("7Y".equalsIgnoreCase(d.getTenor())) {
				t.set_7Y(d.getAmount());
			}
			if ("10Y".equalsIgnoreCase(d.getTenor())) {
				t.set_10Y(d.getAmount());
			}
			if ("15Y".equalsIgnoreCase(d.getTenor())) {
				t.set_15Y(d.getAmount());
			}
			if ("30Y".equalsIgnoreCase(d.getTenor())) {
				t.set_30Y(d.getAmount());
			}
		}

		IntermediateResultEntity iResult = new IntermediateResultEntity();
		iResult.setRiskClass(getRiskClass());
		iResult.setSensitivityType(getSensitivity());
		iResult.setCurrency(trade.getCurrency());
		iResult.setRateIndex(index);
		iResult.setResults(t);
		iResult.setPoResults(results);
		return iResult;
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.IR;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DELTA.toString();
	}
}
