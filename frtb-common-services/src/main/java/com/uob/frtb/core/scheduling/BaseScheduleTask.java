package com.uob.frtb.core.scheduling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseScheduleTask implements FrtbScheduleTask {

	protected ScheduleTaskConfig scheduleTaskConfig;

	@Override
	public void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig) {
		this.scheduleTaskConfig = scheduleTaskConfig;
	}
}
