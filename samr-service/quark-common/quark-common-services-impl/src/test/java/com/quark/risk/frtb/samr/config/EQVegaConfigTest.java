package com.quark.risk.frtb.samr.config;

import com.quark.core.service.test.BaseServiceTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EQVegaConfigTest extends BaseServiceTest {

	@Autowired
	private EQVegaConfig eqVegaConfig;

	@Test
	public void testEQVegaConfig() {
		Assert.assertTrue(eqVegaConfig.getEqVegaSmallCapRw() == 1);
		log.info("EqVegaLargeCapRw : {}", eqVegaConfig.getEqVegaLargeCapRw());
		Assert.assertEquals(eqVegaConfig.getEqVegaLargeCapRw(), 0.777817459, 1e-8);
	}
	
	@Test
	public void testBuildCorrMatForRiskChargeCalc() {
		eqVegaConfig.buildCorrMatForRiskChargeCalc(2, 1);
	}
}
