package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.FXVegaConfig;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class FXVegaRiskChargeCalc extends BaseRiskChargeCalculator {

	private Map<String, CurrencyVega> currVegaMap = new TreeMap<>();

	@Autowired
	private FXVegaConfig fxVegaConfig;

	@Override
	protected void filter() {
		int nRows = 0;
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.FX_VEGA == sens.getSensitivityType()) {
				Vega v = Vega.fromTradeSensitivity(sens);
				CurrencyVega vegaCurr = currVegaMap.get(v.getCurrency());
				if (vegaCurr == null) {
					vegaCurr = new CurrencyVega(v.getCurrency());
					currVegaMap.put(v.getCurrency(), vegaCurr);
				}
				vegaCurr.add(v);
				nRows++;
			}
		}
		log.info("Filtered FX Vega rows : {}", nRows);
		for (Map.Entry<String, CurrencyVega> me : currVegaMap.entrySet()) {
			log.info(me.getValue().toString());
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (currVegaMap.isEmpty()) {
			return;
		}
		List<Double> kb = new ArrayList<>();
		List<Double> sb = new ArrayList<>();
		for (Map.Entry<String, CurrencyVega> me : currVegaMap.entrySet()) {
			log.info("Calculating FX vega risk charge for {}", me.getKey());
			CurrencyVega vegas = me.getValue();
			vegas.calculateMargin();
			kb.add(vegas.getKbBase());
			sb.add(Math.max(Math.min(vegas.getKbBase(), vegas.getSbBase()), -vegas.getKbBase()));
		}

		riskCharge = calculateRiskCharge(fxVegaConfig.getFxCrossBucketCorr(), kb, sb);
		log.info(String.format("FX Vega Margin is {} {}", riskCharge, calcRequest.getBaseCurrency()));
	}

	@Getter
	@ToString
	private class CurrencyVega {
		private String currency;
		private Map<Integer, Vega> vegaMap = new TreeMap<>();
		private double kb;
		private double kbBase;
		private double sb;
		private double sbBase;

		CurrencyVega(String currency) {
			this.currency = currency;
		}

		void calculateMargin() {
			RealMatrix weightedSensitivities = getWeightedSensitivities();
			kb = weightedSensitivities.transpose().multiply(fxVegaConfig.getFxVegaCorr())
					.multiply(weightedSensitivities).getEntry(0, 0);
			kb = Math.sqrt(Math.max(kb, 0));
			double fx = getRate(currency);
			kbBase = kb * fx;
			sbBase = sb * fx;

			log.info("{} : kb : {}, kbBase : {}, sb : {}, sbBase : {} ", currency, kb, kbBase, sb, sbBase);
		}

		private RealMatrix getWeightedSensitivities() {
			double[] optionMaturityTenors = fxVegaConfig.getOptionMaturityTenors();
			int[] optionMaturityTenorCodes = fxVegaConfig.getOptionMaturityTenorCodes();
			double[] vector = new double[optionMaturityTenorCodes.length];
			RealMatrix weightedSensitivities = new Array2DRowRealMatrix(vector.length, 1);
			for (int i = 0; i < optionMaturityTenorCodes.length; i++) {
				Vega d = vegaMap.get(optionMaturityTenorCodes[i]);
				if (d != null) {
					d.scale(fxVegaConfig.getFxVegaRiskWeight() * 100);
					weightedSensitivities.setEntry(i, 0, d.getAmount());
					sb += d.getAmount();
				} else {
					weightedSensitivities.setEntry(i, 0, 0);
					log.error("Vega not found for tenor {}Y", optionMaturityTenors[i]);
				}
			}
			return weightedSensitivities;
		}

		void add(Vega d) {
			Vega d0 = vegaMap.get(d.getTenorCode());
			if (d0 == null) {
				vegaMap.put(d.getTenorCode(), d);
			} else {
				d0.add(d);
			}
		}
	}

	@Getter
	@AllArgsConstructor
	@ToString
	private static class Vega {
		private String currency;
		private String tenor;
		private int tenorCode;
		private double amount;

		void scale(double weight) {
			amount *= weight;
		}

		void add(Vega d) {
			this.amount += d.getAmount();
		}

		static Vega fromTradeSensitivity(TradeSensitivity tradeSens) {
			return new Vega(tradeSens.getCurrency(), tradeSens.getTenor(), tradeSens.getTenorCode(),
					tradeSens.getSensitivity());
		}
	}

	@Override
	void saveResults(PoResults results) {
		log.info("FX Vega - Saving sensitivity results");
		if (currVegaMap.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow vegaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.VEGA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(vegaResult);
		for (CurrencyVega vega : currVegaMap.values()) {
			log.debug("Saving results for currency : {}", vega.getCurrency());
			RiskClassHierarchyResultRow vegaCurrResult = new RiskClassHierarchyResultRow(results, vega.getCurrency(),
					results.getSamrResults().getCurrency(), vega.getCurrency(), vegaResult.buildParentKey(),
					vega.getKb(), vega.getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(vegaCurrResult);
		}
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.FX;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.VEGA.toString();
	}

}
