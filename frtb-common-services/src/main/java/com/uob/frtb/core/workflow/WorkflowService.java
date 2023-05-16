package com.uob.frtb.core.workflow;

import com.uob.frtb.core.security.Organization;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import java.util.List;

public interface WorkflowService {

	List<String> getWorflows();

	List<WorkflowConfig> getWorkflowConfigs(Organization org);

	List<WorkflowConfig> getWorkflowConfigs(String process, Organization org);

	List<WorkflowInstance> getAllWorkflowInstances(Organization organization);

	WorkflowInstance getWorkflowInstance(int excelDate, Long workflowId);

	List<WorkflowInstance> getWorkflowInstances(String workflow, Organization organization);

	WorkflowConfig saveOrUpdate(WorkflowConfig workflow);
}
