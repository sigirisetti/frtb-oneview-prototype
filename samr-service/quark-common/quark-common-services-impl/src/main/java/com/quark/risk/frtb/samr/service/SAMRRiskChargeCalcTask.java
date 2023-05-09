package com.quark.risk.frtb.samr.service;

import com.quark.core.scheduling.BaseScheduleTask;
import com.quark.core.scheduling.ScheduleTask;
import com.quark.core.scheduling.ScheduleTaskGroupsAndNames;

import org.quartz.JobExecutionContext;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@ScheduleTask(group = ScheduleTaskGroupsAndNames.GRP_SAMR, name = ScheduleTaskGroupsAndNames.GRP_SAMR_TASK_RISK_CHARGE_CALC)
public class SAMRRiskChargeCalcTask extends BaseScheduleTask {

	@Override
    public void execute(JobExecutionContext context) {
		log.info("Executing SAMR Calculation task");
	}
}
