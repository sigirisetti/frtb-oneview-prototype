package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.FrtbSensitivities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ResidualsType1RiskChargeCalc extends BaseResidualsRiskChargeCalc {

	@Override
	double getFactor() {
		return config.getType1Factor();
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.RES_TYPE1.toString();
	}

	int getType() {
		return 1;
	}
}
