package com.uob.frtb.core.workflow;

import org.quartz.Job;

public interface IWorkflow extends Job {
	void setWorkflowConfig(WorkflowConfig config);
}
