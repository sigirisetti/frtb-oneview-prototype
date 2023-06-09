package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class IRRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(IRDeltaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(IRVegaRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(IRCurvatureRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.IR;
	}
}
