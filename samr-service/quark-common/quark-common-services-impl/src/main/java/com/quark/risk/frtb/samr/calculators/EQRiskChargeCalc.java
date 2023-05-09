package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class EQRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(EQDeltaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(EQVegaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(EQCurvatureRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.EQ;
	}
}
