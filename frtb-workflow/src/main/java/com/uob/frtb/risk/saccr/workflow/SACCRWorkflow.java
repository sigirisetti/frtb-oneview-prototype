package com.uob.frtb.risk.saccr.workflow;

import com.uob.frtb.core.workflow.IWorkflow;
import com.uob.frtb.core.workflow.Workflow;
import com.uob.frtb.core.workflow.WorkflowConfig;
import com.uob.frtb.core.workflow.WorkflowNames;

import org.activiti.engine.RuntimeService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Workflow(name = WorkflowNames.SACCR_WORKFLOW)
public class SACCRWorkflow implements IWorkflow {

	public static final String FRTB_SACCR_PROCESS = "frtb.saccr.process";
	@Autowired
	private RuntimeService runtimeService;

	private WorkflowConfig config;

	@Override
	public void setWorkflowConfig(WorkflowConfig config) {
		this.config = config;
	}

	@Override
    public void execute(JobExecutionContext context) {
		Map<String, Object> variables = new HashMap<>();
		runtimeService.startProcessInstanceById(FRTB_SACCR_PROCESS, variables);
	}
}
