package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.ResidualsConfig;
import com.uob.frtb.risk.frtb.samr.model.Residuals;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Residual risk charge at trade level applicable to some asset classes and .
 * 
 * Computes risk charge and simple sums at Rate Index level, Currency Bucket
 * level and IR level
 * 
 * @author Surya
 */
@Slf4j
abstract class BaseResidualsRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	protected ResidualsConfig config;

	private List<Residuals> residuals;

	@Override
	protected void filter() {
		residuals = calcRequest.getResiduals();
	}

	@Override
    protected void calculateRiskCharge() {
		for (Residuals r : residuals) {
			if (r.getType() == getType()) {
				riskCharge += r.getNotional() * getFactor();
			}
		}
	}

	abstract int getType();

	abstract double getFactor();

	@Override
	void saveResults(PoResults results) {
		log.info("Residuals risk charge - Saving results");
		if (residuals == null || residuals.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(), getSensitivity(),
				getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.RESIDUAL_RISK;
	}
}
