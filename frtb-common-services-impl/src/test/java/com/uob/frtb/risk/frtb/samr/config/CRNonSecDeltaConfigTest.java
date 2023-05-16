package com.uob.frtb.risk.frtb.samr.config;

import com.uob.frtb.core.service.test.BaseServiceTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CRNonSecDeltaConfigTest extends BaseServiceTest {

	@Autowired
	private CRNonSecDeltaConfig config;

	@Test
	public void testCRNonSecDeltaConfig() {
		Assert.assertTrue(config.getCorr1().getRowDimension() > 0 && config.getCorr1().getColumnDimension() > 0
				&& config.getCorr1().getRowDimension() == config.getCorr1().getColumnDimension());
		Assert.assertTrue(config.getCorr2().getRowDimension() > 0 && config.getCorr2().getColumnDimension() > 0
				&& config.getCorr2().getRowDimension() == config.getCorr2().getColumnDimension());
		Assert.assertTrue(config.getCorr3().getRowDimension() > 0 && config.getCorr3().getColumnDimension() > 0
				&& config.getCorr3().getRowDimension() == config.getCorr3().getColumnDimension());
		Assert.assertTrue(config.getCorr4().getRowDimension() > 0 && config.getCorr4().getColumnDimension() > 0
				&& config.getCorr4().getRowDimension() == config.getCorr4().getColumnDimension());
	}
}
