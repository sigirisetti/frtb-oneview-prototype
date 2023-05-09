package com.quark.risk.frtb.samr.calculators;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DRCSecNonCTPTest extends BaseSamrCalcTest {

	@Test
	public void testRiskChargeCalculation() throws Exception {

		baseCurrency = "USD";
		workflowId = 1L;

		runCalc();
		log.info("Credit Sec Non CTP risk charge : {} ", calc.getRiskCharge());
		//TODO:
		//Assert.assertTrue(calc.getRiskCharge() > 0);
	}

	protected BaseRiskChargeCalculator getRiskChargeCalc() {
		return applicationContext.getBean(DefaultRiskChargeSecNonCTPCalc.class);
	}
}
