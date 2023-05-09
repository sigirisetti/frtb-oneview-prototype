package com.quark.core.workflow;

import com.quark.risk.common.model.WorkflowInstance;

import java.io.Serializable;
import java.util.List;

public interface WorkflowDao {
	void deleteWorkflowInstance(int excelDate, Long workflowId);

	WorkflowInstance getWorkflowInstance(int excelDate, Long workflowId);

	<T extends Serializable> List<T> getWorkflowData(Class<T> c, WorkflowInstance wfInst);

	void deleteWorkflowData(Class<?> c, WorkflowInstance wfInst);
}
