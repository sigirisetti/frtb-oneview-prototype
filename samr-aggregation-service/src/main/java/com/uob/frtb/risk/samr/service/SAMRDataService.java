package com.uob.frtb.risk.samr.service;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.samr.csv.model.SAMRData;
import com.uob.frtb.risk.samr.model.*;
import com.uob.frtb.risk.samr.results.IntermediateResultsData;
import com.uob.frtb.risk.samr.results.PoResults;
import com.uob.frtb.risk.samr.results.RiskClassLevelResults;
import com.uob.frtb.risk.samr.results.RiskChargeResults;

import java.util.List;
import java.util.Map;

public interface SAMRDataService {
	List<Hierarchy> getHierarchy(String workflowInstId);

	void clearData(WorkflowInstance wfInst);

	IntermediateResultsData getIntermediateResults(Integer excelDate, Long workflowId, String nodeId);

	Map<String, List<SAMRData>> getValidationMessages(int excelDate, Long workflowId) throws ApplicationException;

	List<RiskClassLevelResults> getRiskClassLevelResults(WorkflowInstance wfInst);

	List<TradeSensitivity> getTradeSensitivities(WorkflowInstance workflowInst);

	List<SamrMarketQuote> getMarketQuotes(WorkflowInstance workflowInst);

	List<EquityInfo> getEquityInfo(WorkflowInstance workflowInst);

	List<DRCNonSec> getDRCNonSecData(WorkflowInstance workflowInst);

	List<DRCSecNonCTP> getDRCSecNonCTPData(WorkflowInstance workflowInst);

	List<DRCSecCTP> getDRCSecCTPData(WorkflowInstance workflowInst);

	RiskChargeResults getRiskChargeResults(WorkflowInstance workflowInst);

	RiskChargeResults getRiskChargeResults(PoResults samrResults);

	List<CreditIssuerInfo> getCreditIssuerInfo(WorkflowInstance workflowInstance);

	List<Residuals> getResiduals(WorkflowInstance workflowInst);

	CalcRequest buildCalcRequest(WorkflowInstance workflowInstance, DataFilter filter, boolean persist);

	List<CommodityInfo> getCommodityInfo(WorkflowInstance workflowInstance);
}
