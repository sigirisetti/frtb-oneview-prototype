package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class ResidualsRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(ResidualsType1RiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(ResidualsType2RiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.RESIDUAL_RISK;
	}
}
