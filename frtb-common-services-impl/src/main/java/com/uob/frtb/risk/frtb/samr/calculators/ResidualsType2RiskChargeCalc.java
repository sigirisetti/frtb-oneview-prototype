package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ResidualsType2RiskChargeCalc extends BaseResidualsRiskChargeCalc {

	@Override
	double getFactor() {
		return config.getType2Factor();
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.RES_TYPE2.toString();
	}

	int getType() {
		return 2;
	}
}
