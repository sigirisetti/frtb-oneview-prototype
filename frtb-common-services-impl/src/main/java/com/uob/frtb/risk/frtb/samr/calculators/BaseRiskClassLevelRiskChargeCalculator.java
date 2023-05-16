package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.frtb.samr.model.CalcRequest;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;
import com.uob.frtb.risk.frtb.samr.results.RiskClassLevelResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class BaseRiskClassLevelRiskChargeCalculator {

	@Autowired
	protected ApplicationContext applicationContext;

	protected List<BaseRiskChargeCalculator> marginCalculators;

	protected void setupCalculators(CalcRequest samrCalcRequest) {
		for (BaseRiskChargeCalculator calc : marginCalculators) {
			calc.setCalcRequest(samrCalcRequest);
			calc.filter();
		}
	}

	abstract void initCalculators();

	void calculateRiskCharge() throws ApplicationException {
		for (BaseRiskChargeCalculator calc : marginCalculators) {
			calc.calculateRiskCharge();
		}
	}

	abstract RiskClass getRiskClass();

	protected void saveResults(PoResults results) {

		RiskClassHierarchyResultRow riskClassHResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				getRiskClass().toString(), null, 0, 0, 0, 0, 0, 0);
		results.addHierarchyResultRow(riskClassHResult);

		RiskClassLevelResults r = new RiskClassLevelResults();
		r.setRiskClass(getRiskClass());
		r.setPoResults(results);
		for (BaseRiskChargeCalculator calc : marginCalculators) {
			if (FrtbSensitivities.DELTA.toString().equals(calc.getSensitivity())) {
				r.setDelta(calc.getRiskCharge());
			} else if (FrtbSensitivities.VEGA.toString().equals(calc.getSensitivity())) {
				r.setVega(calc.getRiskCharge());
			} else if (FrtbSensitivities.CURVATURE.toString().equals(calc.getSensitivity())) {
				r.setCurvature(calc.getRiskCharge());
			} else if (FrtbSensitivities.DRC_NON_SEC.toString().equals(calc.getSensitivity())) {
				r.setDrcNonSec(calc.getRiskCharge());
			} else if (FrtbSensitivities.DRC_SEC_NON_CTP.toString().equals(calc.getSensitivity())) {
				r.setDrcSecNonCtp(calc.getRiskCharge());
			} else if (FrtbSensitivities.DRC_SEC_CTP.toString().equals(calc.getSensitivity())) {
				r.setDrcSecCtp(calc.getRiskCharge());
			} else if (FrtbSensitivities.RES_TYPE1.toString().equals(calc.getSensitivity())) {
				r.setResType1(calc.getRiskCharge());
			} else if (FrtbSensitivities.RES_TYPE2.toString().equals(calc.getSensitivity())) {
				r.setResType2(calc.getRiskCharge());
			}
			calc.saveResults(results);
		}
		riskClassHResult.setAmountBase(r.getTotalRiskCharge());
		log.info("Risk charge for Risk class : {} is {}", getRiskClass().toString(), r.getTotalRiskCharge());
		results.addRiskClassLevelResults(r);
	}
}
