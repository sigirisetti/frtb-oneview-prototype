package com.quark.web.core.config.scheduling.controller;

import com.quark.core.data.CoreDataService;
import com.quark.core.exception.ApplicationException;
import com.quark.core.scheduling.ScheduleTaskConfig;
import com.quark.core.scheduling.ScheduleTaskService;
import com.quark.core.security.Organization;
import com.quark.web.core.controller.BaseController;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/services/config")
public class ScheduleTaskConfigController extends BaseController {

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private ScheduleTaskService schTaskService;

	@RequestMapping(value = "/getAllScheduledTasks", method = RequestMethod.GET)
	@ResponseBody
	public List<ScheduleTaskConfig> getAllScheduledTasks(HttpServletRequest request) throws ApplicationException {
		Organization org = getOrganization(request);
		return schTaskService.getScheduleTaskConfig(org);
	}

	@RequestMapping(value = "/getAllScheduledTaskTypes", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAllScheduledTaskTypes() {
		return schTaskService.getScheduledTaskNames();
	}

	@RequestMapping(value = "/saveSchTask", method = RequestMethod.POST)

	public ScheduleTaskConfig saveSchTask(@RequestBody final ScheduleTaskConfig task, HttpServletRequest req)
			throws ApplicationException {
		task.setOrganization(getOrganization(req));
		if (!CronExpression.isValidExpression(task.getCronExpression())) {
			throw new ApplicationException(-1, "Invalid cron expression");
		}
		return coreDataService.saveOrUpdate(task);
	}

	@RequestMapping(value = "/deleteSchTask", method = RequestMethod.POST)
	public ScheduleTaskConfig deleteSchTask(@RequestBody final ScheduleTaskConfig task) {
		coreDataService.delete(task);
		return task;
	}
}
