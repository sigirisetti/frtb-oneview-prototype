package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.config.FXCurvatureConfig;
import com.quark.risk.frtb.samr.model.FrtbSensitivities;
import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.quark.risk.frtb.samr.model.TradeSensitivity;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class FXCurvatureRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private FXCurvatureConfig fxCurvConfig;

	private Map<String, CurrencyDelta> currDeltaMap = new TreeMap<>();
	private Map<String, CurrencyCurvature> currCurvMap = new TreeMap<>();

	@Override
	protected void filter() {
		int nRows = 0;
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.FX_CURVATURE == sens.getSensitivityType()) {
				CurrencyDelta cd = fromTradeSensitivity(sens);
				CurrencyDelta currDelta = currDeltaMap.get(cd.getCurrency());
				if (currDelta == null) {
					currDeltaMap.put(cd.getCurrency(), cd);
				} else {
					currDelta.add(cd);
				}
				nRows++;
			}
		}
		log.info("Filtered FX Curvature rows : {}", nRows);
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			log.info(me.getValue().toString());
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (currDeltaMap.isEmpty()) {
			return;
		}
		applyRiskWeightToDelta();
		calculateCurrencyCurvatures();

		double sumOfKbSq = 0;
		double sumSbSq = 0;

		double[] sbs = new double[currCurvMap.size()];
		int k = 0;
		for (Map.Entry<String, CurrencyCurvature> me : currCurvMap.entrySet()) {
			sumOfKbSq += me.getValue().getKbBase() * me.getValue().getKbBase();
			sbs[k++] = me.getValue().getSb();
		}
		for (int i = 0; i < sbs.length; i++) {
			for (int j = 0; j < sbs.length; j++) {
				if (i != j) {
					sumSbSq += sbs[i] * sbs[j];
				}
			}
		}
		riskCharge = Math.sqrt(Math.max(sumOfKbSq + sumSbSq * fxCurvConfig.getFxCurvCurrBucketCorr(), 0));
	}

	private void calculateCurrencyCurvatures() {
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			CurrencyCurvature curv = fromCurrencyDeltas(me.getValue());
			currCurvMap.put(me.getKey(), curv);
		}
		for (Map.Entry<String, CurrencyCurvature> me : currCurvMap.entrySet()) {
			log.info(me.getValue().toString());
		}
	}

	private void applyRiskWeightToDelta() {
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			me.getValue().setDelta(me.getValue().getDelta() * fxCurvConfig.getFxCurvatureRw() * 100);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@ToString
	private class CurrencyCurvature {
		private String currency;
		private double cvrUp;
		private double cvr;
		private double cvrDown;
		private double kb;
		private double kbBase;
		private double sb;
	}

	CurrencyCurvature fromCurrencyDeltas(CurrencyDelta deltas) {
		double cvrUp = getCvrUp(deltas);
		double cvrDown = getCvrDown(deltas);
		double cvr = -Math.min(cvrUp, cvrDown);
		double kb = Math.abs(cvr);
		double kbBase = kb * getRate(deltas.getCurrency());
		return new CurrencyCurvature(deltas.getCurrency(), cvrUp, cvrDown, cvr, kb, kbBase, kbBase);
	}

	double getCvrUp(CurrencyDelta deltas) {
		return (deltas.getMtmUp() - deltas.getMtmBase()) - deltas.getDelta();
	}

	double getCvrDown(CurrencyDelta deltas) {
		return (deltas.getMtmDown() - deltas.getMtmBase()) + deltas.getDelta();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@ToString
	private class CurrencyDelta {
		private String currency;
		private double mtmUp;
		private double mtmBase;
		private double mtmDown;
		private double delta;

		void add(CurrencyDelta d) {
			this.mtmUp += d.mtmUp;
			this.mtmBase += d.mtmBase;
			this.mtmDown += d.mtmDown;
			this.delta += d.delta;
		}
	}

	CurrencyDelta fromTradeSensitivity(TradeSensitivity sens) {
		return new CurrencyDelta(sens.getCurrency(), sens.getMtmBpUpAmount(), sens.getMtmBaseAmount(),
				sens.getMtmBpDownAmount(), sens.getSensitivity());
	}

	@Override
	void saveResults(PoResults results) {
		log.info("FX Curvature - Saving sensitivity results");
		if (currCurvMap.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow curvatureResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.CURVATURE.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(curvatureResult);
		for (Map.Entry<String, CurrencyCurvature> me : currCurvMap.entrySet()) {
			log.debug("Saving results for currency : {}", me.getKey());
			RiskClassHierarchyResultRow curvatureCurrResult = new RiskClassHierarchyResultRow(results, me.getKey(),
					results.getSamrResults().getCurrency(), me.getKey(), curvatureResult.buildParentKey(),
					me.getValue().getKb(), me.getValue().getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(curvatureCurrResult);
		}
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.FX;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.CURVATURE.toString();
	}
}
