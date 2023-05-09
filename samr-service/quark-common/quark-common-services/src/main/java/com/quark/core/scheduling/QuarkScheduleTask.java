package com.quark.core.scheduling;

import org.quartz.Job;

public interface QuarkScheduleTask extends Job {
	void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig);
}
