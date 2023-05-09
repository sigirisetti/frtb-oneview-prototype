package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.results.PoResults;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class CRSecCTPDeltaRiskChargeCalc extends BaseRiskChargeCalculator {

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
