package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

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
