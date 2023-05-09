package com.quark.core.scheduling;

import com.quark.core.data.CoreDataService;
import com.quark.core.security.Organization;
import com.quark.core.workflow.IWorkflow;
import com.quark.core.workflow.WorkflowConfig;
import com.quark.core.workflow.WorkflowService;
import com.quark.risk.common.model.WorkflowInstance;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private WorkflowService workflowService;

	private SchedulerFactory schedulerFactory;

	private Scheduler scheduler;

	// @PostConstruct
	private void scheduleConfiguredTasks() {
		log.info("Scheduling quark tasks");
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			scheduleSimpleTasks();
			scheduleWorkflows();
			scheduler.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void scheduleWorkflows() {
		try {
			List<String> wfNames = workflowService.getWorflows();
			List<WorkflowConfig> tasks = coreDataService.loadAll(WorkflowConfig.class);
			for (String taskName : wfNames) {
				IWorkflow task = (IWorkflow) appCtx.getBean(taskName);
				for (WorkflowConfig config : tasks) {
					if (taskName.equals(config.getProcess())) {
						scheduleWorkflowExecution(task, config);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void scheduleWorkflowExecution(IWorkflow task, WorkflowConfig config) throws SchedulerException {
		JobDetail job = newJob(task.getClass()).withIdentity(config.getName(), config.getName()).build();
		CronTrigger trigger = newTrigger().withIdentity(config.getName(), config.getName())
				.withSchedule(cronSchedule(config.getCronExpression())).build();
		Date ft = scheduler.scheduleJob(job, trigger);
		log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
				+ trigger.getCronExpression());
	}

	private void scheduleSimpleTasks() {
		try {
			List<String> taskNames = getScheduledTaskNames();
			List<ScheduleTaskConfig> tasks = coreDataService.loadAll(ScheduleTaskConfig.class);
			for (String taskName : taskNames) {
				QuarkScheduleTask task = (QuarkScheduleTask) appCtx.getBean(taskName);
				for (ScheduleTaskConfig config : tasks) {
					if (taskName.equals(config.getType())) {
						task.setScheduleTaskConfig(config);
						JobDetail job = newJob(task.getClass()).withIdentity(config.getName(), config.getName())
								.build();
						CronTrigger trigger = newTrigger().withIdentity(config.getName(), config.getName())
								.withSchedule(cronSchedule(config.getCronExpression())).build();
						Date ft = scheduler.scheduleJob(job, trigger);
						log.info(job.getKey() + " has been scheduled to run at: " + ft
								+ " and repeat based on expression: " + trigger.getCronExpression());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<String> getScheduledTaskNames() {
		Map<String, Object> tasks = appCtx.getBeansWithAnnotation(ScheduleTask.class);
		List<String> taskNames = new ArrayList<>();
		for (Map.Entry<String, Object> me : tasks.entrySet()) {
			log.debug("Key : {} , value : {}", me.getKey(), me.getValue());
			Class<? extends Object> fooClass = me.getValue().getClass();
			ScheduleTask annotation = fooClass.getAnnotation(ScheduleTask.class);
			taskNames.add(annotation.name());
		}
		return taskNames;
	}

	@Override
	public List<ScheduleTaskConfig> getScheduleTaskConfig(Organization org) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ScheduleTaskConfig.class)
				.add(Property.forName("organization.id").eq(org.getId()));
		return coreDataService.get(criteria);
	}

	@Override
	public void scheduleWorkflowInstance(WorkflowInstance inst) throws SchedulerException {
		String processName = inst.getWorkflow().getProcess();
		IWorkflow task = (IWorkflow) appCtx.getBean(processName);
		scheduleWorkflowExecution(task, inst.getWorkflow());
	}
}
