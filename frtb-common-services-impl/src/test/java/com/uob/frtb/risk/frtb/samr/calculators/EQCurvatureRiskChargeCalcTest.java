package com.uob.frtb.risk.frtb.samr.calculators;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EQCurvatureRiskChargeCalcTest extends BaseSamrCalcTest {

	@Test
	public void testRunMarginCalculation() throws Exception {

		baseCurrency = "USD";
		workflowId = 1L;

		runCalc();
		log.info("EQ curvature risk charge : {} ", calc.getRiskCharge());
		//Assert.assertTrue(calc.getRiskCharge() > 0);
	}

	protected BaseRiskChargeCalculator getRiskChargeCalc() {
		return applicationContext.getBean(EQCurvatureRiskChargeCalc.class);
	}
}
