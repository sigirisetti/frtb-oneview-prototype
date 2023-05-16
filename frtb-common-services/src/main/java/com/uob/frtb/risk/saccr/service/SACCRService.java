package com.uob.frtb.risk.saccr.service;

import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.saccr.results.SACCRResults;

public interface SACCRService {
	SACCRResults calculate(WorkflowInstance workflowInst);
}
