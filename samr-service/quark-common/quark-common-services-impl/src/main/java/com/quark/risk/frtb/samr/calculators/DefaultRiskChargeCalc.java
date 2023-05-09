package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class DefaultRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(DefaultRiskChargeNonSecCalc.class));
		marginCalculators.add(applicationContext.getBean(DefaultRiskChargeSecNonCTPCalc.class));
		marginCalculators.add(applicationContext.getBean(DefaultRiskChargeSecCTPCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.DEFAULT_RISK;
	}
}
