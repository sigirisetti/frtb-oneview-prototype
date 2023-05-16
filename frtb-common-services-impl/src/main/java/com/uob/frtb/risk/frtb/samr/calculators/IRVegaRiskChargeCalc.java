package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.IRVegaConfig;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * IR vega margin calculator.
 * 
 * Computes risk charge at Currency Bucket level and IR level
 * 
 * @author Surya
 */
@Slf4j
@Component
@Scope("prototype")
public class IRVegaRiskChargeCalc extends BaseRiskChargeCalculator {

	private Map<String, CurrencyVega> currVegaMap = new TreeMap<>();

	@Autowired
	private IRVegaConfig irVegaConfig;

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.IR_VEGA == sens.getSensitivityType()) {
				Vega v = fromTradeSensitivity(sens);
				CurrencyVega vegaCurr = currVegaMap.get(v.getCurrency());
				if (vegaCurr == null) {
					vegaCurr = new CurrencyVega(v.getCurrency());
					currVegaMap.put(v.getCurrency(), vegaCurr);
				}
				vegaCurr.add(v);
			}
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
			log.info("Calculating IR Vega risk charge for {}", me.getKey());
			CurrencyVega vegas = me.getValue();
			vegas.calculateMargin();
			kb.add(vegas.getKbBase());
			sb.add(Math.max(Math.min(vegas.getKbBase(), vegas.getSbBase()), -vegas.getKbBase()));
		}

		riskCharge = calculateRiskCharge(irVegaConfig.getIrCrossBucketCorr(), kb, sb);
		log.info("IR Vega Margin is {} {}", riskCharge, calcRequest.getBaseCurrency());
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	private static class VegaKey {
		private int termCode;
		private int tenorCode;
	}

	@Getter
	@ToString
	private class CurrencyVega {
		private String currency;
		private Map<VegaKey, Vega> vegaMap = new LinkedHashMap<>();
		private double kb;
		private double kbBase;
		private double sb;
		private double sbBase;

		CurrencyVega(String currency) {
			this.currency = currency;
		}

		void add(Vega v) {
			VegaKey key = v.getKey();
			Vega v0 = vegaMap.get(key);
			if (v0 == null) {
				vegaMap.put(key, v);
			} else {
				v0.add(v);
			}
		}

		Array2DRowRealMatrix getRiskWeightedVegaVector() {
			double[] optionMaturityTenors = irVegaConfig.getOptionMaturityTenors();
			int[] optionMaturityTenorCodes = irVegaConfig.getOptionMaturityTenorCodes();
			double[] vector = new double[optionMaturityTenorCodes.length];
			for (int i = 0; i < optionMaturityTenorCodes.length; i++) {
				for (int j = 0; j < optionMaturityTenorCodes.length; j++) {
					Vega v = vegaMap.get(new VegaKey(optionMaturityTenorCodes[i], optionMaturityTenorCodes[j]));
					if (v == null) {
						log.warn("Missing vega for currency {} with tenor {} and maturity {}", currency,
								optionMaturityTenors[i], optionMaturityTenors[j]);
						continue;
					}
					vector[i] = vector[i] + v.getAmount();
				}
			}
			for (int i = 0; i < vector.length; i++) {
				vector[i] *= irVegaConfig.getRw();
			}
			return new Array2DRowRealMatrix(vector);
		}

		void calculateMargin() {
			RealMatrix vega = getRiskWeightedVegaVector();
			RealMatrix corr = irVegaConfig.getIRVegaCorr();
			kb = vega.transpose().multiply(corr).multiply(vega).getEntry(0, 0);
			kb = Math.sqrt(kb);
			double fx = getRate(currency);
			kbBase = kb * fx;
			for (int i = 0; i < vega.getRowDimension(); i++) {
				sb += vega.getEntry(i, 0);
			}
			sbBase = sb * fx;
		}
	}

	@Getter
	@AllArgsConstructor
	private class Vega {
		private String currency;
		private String index;
		private String term;
		private int termCode;
		private String tenor;
		private int tenorCode;
		private double amount;

		public void add(Vega d) {
			this.amount += d.getAmount();
		}

		VegaKey getKey() {
			return new VegaKey(termCode, tenorCode);
		}
	}

	Vega fromTradeSensitivity(TradeSensitivity tradeSens) {
		return new Vega(tradeSens.getCurrency(), tradeSens.getIndex(), tradeSens.getTerm(), tradeSens.getTermCode(),
				tradeSens.getTenor(), tradeSens.getTenorCode(), tradeSens.getSensitivity());
	}

	@Override
	void saveResults(PoResults results) {
		log.info("IR Vega - Saving sensitivity results");
		if (currVegaMap.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow vegaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.VEGA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(vegaResult);
		for (CurrencyVega riCol : currVegaMap.values()) {
			log.debug("Saving results for currency : {}", riCol.getCurrency());
			RiskClassHierarchyResultRow vegaCurrResult = new RiskClassHierarchyResultRow(results, riCol.getCurrency(),
					results.getSamrResults().getCurrency(), riCol.getCurrency(), vegaResult.buildParentKey(),
					riCol.getKb(), riCol.getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(vegaCurrResult);
		}
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.IR;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.VEGA.toString();
	}
}
