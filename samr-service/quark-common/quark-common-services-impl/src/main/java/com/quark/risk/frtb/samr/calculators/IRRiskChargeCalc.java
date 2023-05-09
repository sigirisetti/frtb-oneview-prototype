package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;

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
