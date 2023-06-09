package com.uob.frtb.web.risk.saccr.controller;

import com.uob.frtb.core.data.CoreDataService;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.security.User;
import com.uob.frtb.core.workflow.WorkflowNames;
import com.uob.frtb.core.workflow.WorkflowService;
import com.uob.frtb.risk.common.model.WorkflowInstExecStatus;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.saccr.service.SACCRDataService;
import com.uob.frtb.web.core.controller.BaseController;
import com.uob.frtb.web.file.upload.utils.FileUploadHelper;
import com.uob.frtb.web.file.upload.utils.FileUploadResponse;
import com.uob.frtb.web.risk.frtb.samr.view.model.FileUploadForm;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/services/saccr")
public class SACCRServiceController extends BaseController {

	@Autowired
	private SACCRDataService saccrDataService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private CoreDataService coreDataService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public FileUploadResponse upload(@ModelAttribute("uploadForm") FileUploadForm uploadForm, HttpServletRequest req)
            throws ApplicationException {
		FileUploadHelper fuHelper = appCtx.getBean(FileUploadHelper.class);
		fuHelper.saveUploads("saccr", getOrganization(req), uploadForm);
		WorkflowInstance wfInst = fuHelper.getWorkflowInst();
		saveWorkflowInstance(wfInst);
		triggerWorkflow(wfInst);
		return fuHelper.getResponse();
	}

	@RequestMapping(value = "/getAllSACCRWorkflowInstances", method = RequestMethod.GET)
	@ResponseBody
	public List<WorkflowInstance> getAllSAMRWorkflowInstances(HttpServletRequest req) throws ApplicationException {
		User u = getUser(req);
		List<WorkflowInstance> instances = workflowService
				.getWorkflowInstances(WorkflowNames.SACCR_WORKFLOW, u.getOrganization());
		for (WorkflowInstance inst : instances) {
			inst.setDate(DateUtil.getJavaDate(inst.getExcelDate()));
		}
		return instances;
	}

	private void triggerWorkflow(WorkflowInstance workflowInst) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("workflowInst", workflowInst);
		variables.put("persist", true);
		workflowTrigger.triggerWorkflow("frtb.saccr.process", variables);
		workflowInst.setStatus(WorkflowInstExecStatus.COMPLETED);
		coreDataService.saveOrUpdate(workflowInst);
	}

	private void saveWorkflowInstance(WorkflowInstance workflowInst) {
		WorkflowInstance old = workflowService.getWorkflowInstance(workflowInst.getExcelDate(),
				workflowInst.getWorkflow().getId());
		if (old != null) {
			saccrDataService.clearData(old);
		}
		workflowInst.setStatus(WorkflowInstExecStatus.PENDING);
		coreDataService.save(workflowInst);
	}
}
