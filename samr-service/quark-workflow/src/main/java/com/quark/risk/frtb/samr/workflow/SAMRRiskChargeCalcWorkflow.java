package com.quark.risk.frtb.samr.workflow;

import com.quark.core.workflow.IWorkflow;
import com.quark.core.workflow.Workflow;
import com.quark.core.workflow.WorkflowConfig;
import com.quark.core.workflow.WorkflowNames;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Workflow(name = WorkflowNames.SAMR_RISK_CHARGE_CALC_WORKFLOW)
public class SAMRRiskChargeCalcWorkflow implements IWorkflow {

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
		ProcessInstance processInstance = runtimeService.startProcessInstanceById("quark.samr.process", variables);
	}
}
