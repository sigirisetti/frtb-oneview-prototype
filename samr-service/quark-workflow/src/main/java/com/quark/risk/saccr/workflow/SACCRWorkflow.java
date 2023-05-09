package com.quark.risk.saccr.workflow;

import com.quark.core.workflow.IWorkflow;
import com.quark.core.workflow.Workflow;
import com.quark.core.workflow.WorkflowConfig;
import com.quark.core.workflow.WorkflowNames;

import org.activiti.engine.RuntimeService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Workflow(name = WorkflowNames.SACCR_WORKFLOW)
public class SACCRWorkflow implements IWorkflow {

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
		runtimeService.startProcessInstanceById("quark.saccr.process", variables);
	}
}
