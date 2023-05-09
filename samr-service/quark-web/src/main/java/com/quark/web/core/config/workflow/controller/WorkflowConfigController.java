package com.quark.web.core.config.workflow.controller;

import com.quark.core.data.CoreDataService;
import com.quark.core.exception.ApplicationException;
import com.quark.core.security.Organization;
import com.quark.core.security.User;
import com.quark.core.workflow.WorkflowConfig;
import com.quark.core.workflow.WorkflowNames;
import com.quark.core.workflow.WorkflowService;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.web.core.controller.BaseController;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/services/config")
public class WorkflowConfigController extends BaseController {

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private WorkflowService workflowService;

    @GetMapping(value = "/getAllWorkflows")
	@ResponseBody
	public List<WorkflowConfig> getAllWorkflows(HttpServletRequest request) throws ApplicationException {
		Organization org = getOrganization(request);
		return workflowService.getWorkflowConfigs(org);
	}

	@RequestMapping(value = "/getSAMRWorkflows", method = RequestMethod.GET)
	@ResponseBody
	public List<WorkflowConfig> getSAMRWorkflows(HttpServletRequest request) throws ApplicationException {
		Organization org = getOrganization(request);
		return workflowService.getWorkflowConfigs(WorkflowNames.SAMR_RISK_CHARGE_CALC_WORKFLOW, org);
	}

	@RequestMapping(value = "/getSACCRWorkflows", method = RequestMethod.GET)
	@ResponseBody
	public List<WorkflowConfig> getSACCRWorkflows(HttpServletRequest request) throws ApplicationException {
		Organization org = getOrganization(request);
		return workflowService.getWorkflowConfigs(WorkflowNames.SACCR_WORKFLOW, org);
	}

    @GetMapping(value = "/getAllProcesses")
	@ResponseBody
	public List<String> getAllProcesses() {
		return workflowService.getWorflows();
	}

	@RequestMapping(value = "/saveWorkflow", method = RequestMethod.POST)
	public WorkflowConfig saveWorkflow(@RequestBody final WorkflowConfig Workflow, HttpServletRequest req)
			throws ApplicationException {
		Workflow.setOrganization(getOrganization(req));
		if(!CronExpression.isValidExpression(Workflow.getCronExpression())) {
			throw new ApplicationException(-1, "Invalid cron expression");
		}
		
		return workflowService.saveOrUpdate(Workflow);
	}

	@RequestMapping(value = "/deleteWorkflow", method = RequestMethod.POST)
	public WorkflowConfig deleteWorkflow(@RequestBody final WorkflowConfig Workflow) {
		coreDataService.delete(Workflow);
		return Workflow;
	}

	@RequestMapping(value = "/getAllWorkflowInstances", method = RequestMethod.GET)
	@ResponseBody
	public List<WorkflowInstance> getAllWorkflowInstances(HttpServletRequest req)
			throws ApplicationException {
		User u = getUser(req);
		return workflowService.getAllWorkflowInstances(u.getOrganization());
	}
}
