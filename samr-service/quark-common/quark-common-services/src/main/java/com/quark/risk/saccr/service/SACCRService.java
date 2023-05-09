package com.quark.risk.saccr.service;

import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.saccr.results.SACCRResults;

public interface SACCRService {
	SACCRResults calculate(WorkflowInstance workflowInst);
}
