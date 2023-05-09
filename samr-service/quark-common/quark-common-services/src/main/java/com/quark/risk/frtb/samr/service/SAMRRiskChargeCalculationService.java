package com.quark.risk.frtb.samr.service;

import com.quark.core.exception.ApplicationException;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.frtb.samr.model.DataFilter;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.SAMRResults;

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
