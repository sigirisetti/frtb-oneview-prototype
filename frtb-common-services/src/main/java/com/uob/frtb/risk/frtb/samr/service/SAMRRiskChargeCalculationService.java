package com.uob.frtb.risk.frtb.samr.service;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.frtb.samr.model.DataFilter;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.SAMRResults;

import java.io.File;
import java.util.List;

public interface SAMRRiskChargeCalculationService {

	SAMRResults calculateMargins(WorkflowInstance workflowInst, boolean persist) throws ApplicationException;

	PoResults calculateMarginsApplyingFilter(WorkflowInstance workflowInstance, DataFilter filter)
			throws ApplicationException;

	File getFileUploadDir(int excelDate, Long workflowId, String email) throws ApplicationException;

	List<PoResults> calculateTradeLevelRiskCharge(WorkflowInstance workflowInstance, boolean persist)
			throws ApplicationException;
}
