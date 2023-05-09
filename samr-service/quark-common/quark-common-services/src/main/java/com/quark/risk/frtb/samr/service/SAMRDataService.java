package com.quark.risk.frtb.samr.service;

import com.quark.core.exception.ApplicationException;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.frtb.samr.csv.model.SAMRData;
import com.quark.risk.frtb.samr.model.CalcRequest;
import com.quark.risk.frtb.samr.model.CommodityInfo;
import com.quark.risk.frtb.samr.model.CreditIssuerInfo;
import com.quark.risk.frtb.samr.model.DRCNonSec;
import com.quark.risk.frtb.samr.model.DRCSecCTP;
import com.quark.risk.frtb.samr.model.DRCSecNonCTP;
import com.quark.risk.frtb.samr.model.DataFilter;
import com.quark.risk.frtb.samr.model.EquityInfo;
import com.quark.risk.frtb.samr.model.Hierarchy;
import com.quark.risk.frtb.samr.model.SamrMarketQuote;
import com.quark.risk.frtb.samr.model.Residuals;
import com.quark.risk.frtb.samr.model.TradeSensitivity;
import com.quark.risk.frtb.samr.results.IntermediateResultsData;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.RiskClassLevelResults;
import com.quark.risk.frtb.samr.results.model.RiskChargeResults;

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
