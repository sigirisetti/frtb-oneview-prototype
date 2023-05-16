package com.uob.frtb.risk.samr.dao;

import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.samr.model.DRCNonSec;
import com.uob.frtb.risk.samr.model.DRCSecCTP;
import com.uob.frtb.risk.samr.model.DRCSecNonCTP;
import com.uob.frtb.risk.samr.model.DataFilter;
import com.uob.frtb.risk.samr.model.EquityInfo;
import com.uob.frtb.risk.samr.model.Hierarchy;
import com.uob.frtb.risk.samr.model.Residuals;
import com.uob.frtb.risk.samr.model.SamrMarketQuote;
import com.uob.frtb.risk.samr.model.TradeSensitivity;
import com.uob.frtb.risk.samr.results.IntermediateResultEntity;

import java.util.List;
import java.util.Map;

public interface SAMRDao {

	List<TradeSensitivity> getTradeSensitivities(String workflowId);

	List<TradeSensitivity> getTradeSensitivities(String workflowId, DataFilter filter);

	List<SamrMarketQuote> getMarketQuotes(String workflowId);

	List<Hierarchy> getHierarchy(String wfInstId);

	void deleteTradeSensitivities(WorkflowInstance wfInst);

	void deleteDRCNonSec(WorkflowInstance wfInst);

	void deleteEquityInfo(WorkflowInstance wfInst);

	void deleteHierarchy(WorkflowInstance wfInst);

	void deleteMarketQuote(WorkflowInstance wfInst);

	void deleteSAMRResults(WorkflowInstance wfInst);

	void deleteIntermediateResults(WorkflowInstance wfInst);

	List<IntermediateResultEntity> getIntermediateResults(String workflowInstance, String po, String riskClass,
														  String sensType, String currency, String rateIndex);

	void deleteValidationMessages(WorkflowInstance wfInst);

	List<EquityInfo> getEquityInfo(String workflowId);

	List<DRCNonSec> getDRCNonSecData(String id);

	void deleteDRCSecNonCTP(WorkflowInstance wfInst);

	void deleteDRCSecCTP(WorkflowInstance wfInst);

	List<DRCSecNonCTP> getDRCSecNonCTPData(String workflowId);

	List<DRCSecCTP> getDRCSecCTPData(String workflowId);

	List<DRCNonSec> getDRCNonSecData(String id, DataFilter filter);

	List<DRCSecNonCTP> getDRCSecNonCTPData(String workflowId, DataFilter filter);

	List<DRCSecCTP> getDRCSecCTPData(String workflowId, DataFilter filter);

	List<Residuals> getResiduals(String workflowId);

	List<Residuals> getResiduals(String id, DataFilter filter);

	void deleteResiduals(WorkflowInstance wfInst);

	void deleteCreditIssuerInfo(WorkflowInstance wfInst);

	List<String> getPo(String wfInstId);

	Map<String, List<String>> groupTradeIdsByPo(String wfInstId);

	void deleteCommodityInfo(WorkflowInstance wfInst);

	String getPoResultsId(String workflowInstance, String po);
}
