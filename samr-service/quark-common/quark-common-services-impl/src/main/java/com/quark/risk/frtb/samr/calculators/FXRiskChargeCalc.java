package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class FXRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(FXDeltaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(FXVegaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(FXCurvatureRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.FX;
	}
}
