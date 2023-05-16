package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class COMMRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(COMMDeltaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(COMMVegaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(COMMCurvatureRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.COMMODITY;
	}
}