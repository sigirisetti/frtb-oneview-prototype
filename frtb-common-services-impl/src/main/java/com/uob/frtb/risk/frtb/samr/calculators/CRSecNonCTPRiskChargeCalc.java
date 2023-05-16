package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.model.RiskClass;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Scope("prototype")
public class CRSecNonCTPRiskChargeCalc  extends BaseRiskClassLevelRiskChargeCalculator {

	void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(CRSecNonCTPDeltaRiskChargeCalc.class));
	}

	@Override
    RiskClass getRiskClass() {
		return RiskClass.CREDIT_S_NCTP;
	}
}