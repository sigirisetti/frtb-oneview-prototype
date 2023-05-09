package com.quark.core.scheduling;

import com.quark.core.security.Organization;
import com.quark.risk.common.model.WorkflowInstance;

import org.quartz.SchedulerException;

import java.util.List;

public interface ScheduleTaskService {
	List<String> getScheduledTaskNames();

	List<ScheduleTaskConfig> getScheduleTaskConfig(Organization org);

	void scheduleWorkflowInstance(WorkflowInstance inst) throws SchedulerException;
}
