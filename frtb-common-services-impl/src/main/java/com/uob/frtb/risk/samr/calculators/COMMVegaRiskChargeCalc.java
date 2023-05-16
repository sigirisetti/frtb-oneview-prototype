package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.model.RiskClass;
import com.uob.frtb.risk.samr.results.PoResults;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class COMMVegaRiskChargeCalc extends BaseRiskChargeCalculator {

	@Override
	protected void filter() {
		// TODO Auto-generated method stub

	}

	@Override
    protected void calculateRiskCharge() {
		// TODO Auto-generated method stub

	}

	@Override
	void saveResults(PoResults results) {
		// TODO Auto-generated method stub

	}

	@Override
	String getSensitivity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    RiskClass getRiskClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
