package com.uob.frtb.core.scheduling;

import com.uob.frtb.core.security.Organization;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import org.quartz.SchedulerException;

import java.util.List;

public interface ScheduleTaskService {
	List<String> getScheduledTaskNames();

	List<ScheduleTaskConfig> getScheduleTaskConfig(Organization org);

	void scheduleWorkflowInstance(WorkflowInstance inst) throws SchedulerException;
}
