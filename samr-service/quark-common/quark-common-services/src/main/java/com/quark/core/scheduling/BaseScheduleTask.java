package com.quark.core.scheduling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseScheduleTask implements QuarkScheduleTask {

	protected ScheduleTaskConfig scheduleTaskConfig;

	@Override
	public void setScheduleTaskConfig(ScheduleTaskConfig scheduleTaskConfig) {
		this.scheduleTaskConfig = scheduleTaskConfig;
	}
}
