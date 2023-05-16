package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class CRNonSecRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(CRNonSecDeltaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(CRNonSecVegaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(CRNonSecCurvatureRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.CREDIT_NS;
	}
}