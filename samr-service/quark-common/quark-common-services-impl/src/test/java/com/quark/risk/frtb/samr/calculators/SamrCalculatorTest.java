package com.quark.risk.frtb.samr.calculators;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SamrCalculatorTest extends BaseSamrCalcTest {

	private static final String VALUE_DATE = "25-Sep-2016";

	@Test
	public void testRunMarginCalculation() throws Exception {
		/*
		int excelDate = (int) DateUtil.getExcelDate(new SimpleDateFormat("dd-MMM-yyyy").parse(VALUE_DATE));
		Long workflowId = 1L;
		WorkflowInstance workflowInstance = workflowService.getWorkflowInstance(excelDate, workflowId);
		if (workflowInstance == null) {
			throw new ApplicationException(-1, "SAMR calculation not performed for given value date");
		}
		samrDataService.clearData(workflowInstance);
		SAMRResults results = samrService.calculateMargins(workflowInstance, false);
		Assert.assertNotNull(results);
	}

	@Test
	public void testTradeLevelRiskCharge() throws Exception {
		int excelDate = (int) DateUtil.getExcelDate(new SimpleDateFormat("dd-MMM-yyyy").parse(VALUE_DATE));
		Long workflowId = 1L;
		WorkflowInstance workflowInstance = workflowService.getWorkflowInstance(excelDate, workflowId);
		if (workflowInstance == null) {
			throw new ApplicationException(-1, "SAMR calculation not performed for given value date");
		}
		// samrDataService.clearData(workflowInstance);
		List<PoResults> results = samrService.calculateTradeLevelRiskCharge(workflowInstance, false);
		Assert.assertNotNull(results);

		 */
	}

	@Override
	protected BaseRiskChargeCalculator getRiskChargeCalc() {
		return null;
	}
}
