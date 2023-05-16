package com.uob.frtb.core.scheduling;

import org.quartz.Job;

public interface FrtbScheduleTask extends Job {
	void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig);
}
