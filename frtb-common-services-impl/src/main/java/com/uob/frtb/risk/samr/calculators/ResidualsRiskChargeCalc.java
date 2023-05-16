package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

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
