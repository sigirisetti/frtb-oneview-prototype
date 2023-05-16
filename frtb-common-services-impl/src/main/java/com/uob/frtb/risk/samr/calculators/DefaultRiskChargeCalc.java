package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

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
