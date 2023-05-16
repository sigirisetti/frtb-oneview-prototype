package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class CRSecCTPRiskChargeCalc extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(CRSecCTPDeltaRiskChargeCalc.class));
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.CREDIT_S_CTP;
	}
}