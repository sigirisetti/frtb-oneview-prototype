package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.FrtbSensitivities;

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
