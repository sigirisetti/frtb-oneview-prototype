package com.uob.frtb.risk.samr.service;

import com.uob.frtb.core.scheduling.BaseScheduleTask;
import com.uob.frtb.core.scheduling.ScheduleTask;
import com.uob.frtb.core.scheduling.ScheduleTaskGroupsAndNames;

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
