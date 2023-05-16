package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.config.IRCurvatureConfig;
import com.uob.frtb.risk.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.samr.model.RiskClass;
import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.samr.model.TradeSensitivity;
import com.uob.frtb.risk.samr.results.PoResults;
import com.uob.frtb.risk.samr.results.RiskClassHierarchyResultRow;

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
public class IRCurvatureRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private IRCurvatureConfig irCurvConfig;

	private Map<String, CurrencyDelta> currDeltaMap = new TreeMap<>();
	private Map<String, CurrencyCurvature> currCurvMap = new TreeMap<>();

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.IR_CURVATURE == sens.getSensitivityType()) {
				CurrencyDelta cd = fromTradeSensitivity(sens);
				CurrencyDelta currDelta = currDeltaMap.get(cd.getCurrency());
				if (currDelta == null) {
					currDeltaMap.put(cd.getCurrency(), cd);
				} else {
					currDelta.add(cd);
				}
			}
		}
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			log.info(me.getValue().toString());
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (currDeltaMap.isEmpty()) {
			return;
		}
		applyRiskWeightToMtmBase();
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
		riskCharge = Math.sqrt(Math.max(sumOfKbSq + sumSbSq * irCurvConfig.getIrCurvCurrBucketCorr(), 0));
	}

	private void calculateCurrencyCurvatures() {
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			CurrencyCurvature curv = fromCurrencyDeltas(me.getValue());
			currCurvMap.put(me.getKey(), curv);
		}
	}

	private void applyRiskWeightToMtmBase() {
		for (Map.Entry<String, CurrencyDelta> me : currDeltaMap.entrySet()) {
			me.getValue().setDelta(me.getValue().getDelta() * irCurvConfig.getIrCurvatureRw() * 10000);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
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
		log.info("IR Curvature - Saving sensitivity results");
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
		return RiskClass.IR;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.CURVATURE.toString();
	}
}
