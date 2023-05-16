package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.service.test.BaseServiceTest;
import com.uob.frtb.core.workflow.WorkflowService;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.frtb.samr.model.CalcRequest;
import com.uob.frtb.risk.frtb.samr.service.SAMRDataService;
import com.uob.frtb.risk.frtb.samr.service.SAMRRiskChargeCalculationService;

import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseSamrCalcTest extends BaseServiceTest {

	protected static final String DD_MMM_YYYY = "dd-MMM-yyyy";

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected SAMRRiskChargeCalculationService samrService;

	@Autowired
	protected SAMRDataService samrDataService;

	protected WorkflowService workflowService;

	protected BaseRiskChargeCalculator calc;

	protected String baseCurrency;
    protected String valueDate = "14-Jun-2019";
	protected Long workflowId;

    @Before
    public void init() {
        workflowService = mock(WorkflowService.class);
        WorkflowInstance wi = new WorkflowInstance();
        wi.setId("1");
        when(workflowService.getWorkflowInstance(anyInt(), anyLong())).thenReturn(wi);
    }

	protected void runCalc() throws ParseException, ApplicationException {
		CalcRequest req = setupRiskChargeCalc();
		calc = getRiskChargeCalc();
		calc.setCalcRequest(req);
		calc.filter();
		calc.calculateRiskCharge();
	}

	protected CalcRequest setupRiskChargeCalc() throws ParseException {
		int excelDate = getExcelDate();
		WorkflowInstance workflowInstance = getWorkflowInstance(excelDate);
		Assert.assertNotNull(workflowInstance);
		return samrDataService.buildCalcRequest(workflowInstance, null, false);
	}

    protected int getExcelDate() {
        int excelDate = 0;
        try {
            excelDate = (int) DateUtil.getExcelDate(new SimpleDateFormat(DD_MMM_YYYY).parse(valueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return excelDate;
	}

	protected WorkflowInstance getWorkflowInstance(int excelDate) {
		return workflowService.getWorkflowInstance(excelDate, workflowId);
	}

	protected abstract BaseRiskChargeCalculator getRiskChargeCalc();
}
