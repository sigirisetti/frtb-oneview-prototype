package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.config.FXDeltaConfig;
import com.quark.risk.frtb.samr.model.FrtbSensitivities;
import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.quark.risk.frtb.samr.model.TradeSensitivity;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class FXDeltaRiskChargeCalc extends BaseRiskChargeCalculator {

	private Map<String, Delta> currDeltaMap = new TreeMap<>();

	@Autowired
	private FXDeltaConfig fxDeltaConfig;

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.FX_DELTA == sens.getSensitivityType()) {
				Delta d = new Delta(sens.getCurrency(), sens.getSensitivity());
				Delta currDelta = currDeltaMap.get(d.getCurrency());
				if (currDelta == null) {
					currDeltaMap.put(d.getCurrency(), d);
				} else {
					currDelta.add(d);
				}
			}
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (currDeltaMap.isEmpty()) {
			return;
		}
		double[] delta = new double[currDeltaMap.size()];
		int i = 0;
		for (Map.Entry<String, Delta> me : currDeltaMap.entrySet()) {
			me.getValue().computeRiskCharge();
			delta[i++] = me.getValue().getKbBase();
		}

		RealMatrix deltaVector = new Array2DRowRealMatrix(delta);
		riskCharge = deltaVector.transpose().multiply(fxDeltaConfig.getFXCorr(currDeltaMap.size()))
				.multiply(deltaVector).getEntry(0, 0);
		riskCharge = Math.sqrt(riskCharge);
		log.info("FX Delta Margin is {} {}", riskCharge, calcRequest.getBaseCurrency());
	}

	@Getter
	private class Delta {
		private String currency;
		private double amount;
		private double kb;
		private double kbBase;
		private double sb;
		private double sbBase;

		Delta(String currency, double amount) {
			this.currency = currency;
			this.amount = amount;
		}

		void computeRiskCharge() {
			double rwDelta = amount * fxDeltaConfig.getFxDeltaRiskWeight() * 100;
			kb = Math.abs(rwDelta);
			kbBase = kb * getRate(currency);
		}

		void add(Delta d) {
			this.amount += d.getAmount();
		}
	}

	@Override
	void saveResults(PoResults results) {
		log.info("FX Delta - Saving results");
		if (currDeltaMap.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DELTA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
		for (Delta delta : currDeltaMap.values()) {
			log.debug("Saving results for currency : {}", delta.getCurrency());
			RiskClassHierarchyResultRow deltaCurrResult = new RiskClassHierarchyResultRow(results, delta.getCurrency(),
					results.getSamrResults().getCurrency(), delta.getCurrency(), deltaResult.buildParentKey(),
					delta.getKb(), delta.getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(deltaCurrResult);
		}
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.FX;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DELTA.toString();
	}
}
