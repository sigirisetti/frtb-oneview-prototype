package com.quark.web.risk.frtb.samr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quark.core.data.CoreDataService;
import com.quark.core.exception.ApplicationException;
import com.quark.core.security.User;
import com.quark.core.workflow.WorkflowNames;
import com.quark.core.workflow.WorkflowService;
import com.quark.risk.common.model.WorkflowInstExecStatus;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.frtb.samr.csv.model.SAMRData;
import com.quark.risk.frtb.samr.model.DataFilter;
import com.quark.risk.frtb.samr.model.Hierarchy;
import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.results.IntermediateResultsData;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.RiskClassLevelResults;
import com.quark.risk.frtb.samr.results.model.RiskChargeResults;
import com.quark.risk.frtb.samr.service.SAMRDataService;
import com.quark.risk.frtb.samr.service.SAMRRiskChargeCalculationService;
import com.quark.web.core.controller.BaseController;
import com.quark.web.file.upload.utils.FileUploadHelper;
import com.quark.web.file.upload.utils.FileUploadResponse;
import com.quark.web.risk.frtb.samr.view.model.FileUploadForm;
import com.quark.web.risk.frtb.samr.view.model.Nvd3DashboardData;
import com.quark.web.risk.frtb.samr.view.model.ResultsQueryForm;
import com.quark.web.risk.frtb.samr.view.model.TradeLevelRiskChargeResults;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/services/samr")
public class SAMRServiceController extends BaseController {

	@Autowired
	private SAMRRiskChargeCalculationService samrService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private SAMRDataService samrDataService;

	/**
	 * Serves SAMR margin calculations on default hierarchy
	 *
	 * old name defaultSAMRView
	 * 
	 * @param valueDate
	 * @param workflowId
	 * @param req
	 * @return
	 * @throws JsonProcessingException
	 * @throws ApplicationException
	 */
	@RequestMapping(value = "/getSAMRExecResults", method = RequestMethod.GET)
	@ResponseBody
	public RiskChargeResults getSAMRExecResults(@RequestParam String valueDate, @RequestParam Long workflowId, HttpServletRequest req)
			throws JsonProcessingException, ApplicationException {
		WorkflowInstance workflowInstance = getWorkflowInstance(valueDate, workflowId);
		RiskChargeResults results = samrDataService.getRiskChargeResults(workflowInstance);

		String json = toJson(results);
		if (StringUtils.isBlank(json)) {
			throw new ApplicationException(-1,
					String.format("Failed to calculate margins for value date : %s", valueDate));
		}
		log.info("SAMR Margins : " + json);
		return results;
	}

	private WorkflowInstance getWorkflowInstance(String valueDate, Long workflowId) throws ApplicationException {
		int excelDate = getExcelDate(valueDate);
		WorkflowInstance workflowInstance = workflowService.getWorkflowInstance(excelDate, workflowId);
		if (workflowInstance == null) {
			throw new ApplicationException(-1, "SAMR calculation not performed for given value date");
		}
		return workflowInstance;
	}

	private String getWorkflowId(int excelDate, Long workflowId) {
		WorkflowInstance inst = workflowService.getWorkflowInstance(excelDate, workflowId);
		if (inst != null) {
			return workflowService.getWorkflowInstance(excelDate, workflowId).getId();
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/filteredSAMRView", method = RequestMethod.GET)
	@ResponseBody
	public String filteredSAMRView(@RequestParam String valueDate, @RequestParam Long workflowId,
			@RequestParam String po, @RequestParam String filterName, @RequestParam String filterValue,
			HttpServletRequest req) throws JsonProcessingException, ApplicationException {
		WorkflowInstance workflowInstance = getWorkflowInstance(valueDate, workflowId);
		DataFilter filter = new DataFilter(po, filterName, filterValue);
		PoResults results = samrService.calculateMarginsApplyingFilter(workflowInstance, filter);
		RiskChargeResults rcResults = samrDataService.getRiskChargeResults(results);
		String json = toJson(rcResults);
		if (StringUtils.isBlank(json)) {
			throw new ApplicationException(-1,
					String.format("Failed to calculate margins for value date : %s", valueDate));
		}
		log.info("SAMR Margins : " + json);
		return json;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public FileUploadResponse upload(@ModelAttribute("uploadForm") FileUploadForm uploadForm, HttpServletRequest req)
            throws ApplicationException {
		FileUploadHelper fuHelper = appCtx.getBean(FileUploadHelper.class);
		fuHelper.saveUploads("samr", getOrganization(req), uploadForm);
		WorkflowInstance wfInst = fuHelper.getWorkflowInst();
		saveWorkflowInstance(wfInst);
		triggerWorkflow(wfInst);
		return fuHelper.getResponse();
	}

	private void saveWorkflowInstance(WorkflowInstance workflowInst) {
		WorkflowInstance old = workflowService.getWorkflowInstance(workflowInst.getExcelDate(),
				workflowInst.getWorkflow().getId());
		if (old != null) {
			samrDataService.clearData(old);
		}
		workflowInst.setStatus(WorkflowInstExecStatus.PENDING);
		coreDataService.save(workflowInst);
	}

	@Async
	private void triggerWorkflow(WorkflowInstance workflowInst) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("workflowInst", workflowInst);
		variables.put("persist", true);
		workflowTrigger.triggerWorkflow("quark.samr.process", variables);
		workflowInst.setStatus(WorkflowInstExecStatus.COMPLETED);
		coreDataService.saveOrUpdate(workflowInst);
	}

	@RequestMapping(value = "/view/validation/msgs", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, List<SAMRData>> viewValidationMessages(@RequestBody ResultsQueryForm query) throws Exception {
		return samrDataService.getValidationMessages(getExcelDate(query.getValueDate()), query.getWorkflowId());
	}

	@RequestMapping(value = "/getHierarchy", method = RequestMethod.GET)
	@ResponseBody
	public com.quark.risk.frtb.samr.results.model.Hierarchy getHierarchy(@RequestParam String valueDate,
			@RequestParam Long workflowId, HttpServletRequest req) throws ApplicationException {
		int excelDate = getExcelDate(valueDate);
		String workflowInstanceId = getWorkflowId(excelDate, workflowId);
		if (workflowInstanceId == null) {
			throw new ApplicationException(-1, "SAMR calculation not performed for given value date");
		}
		List<Hierarchy> hData = samrDataService.getHierarchy(workflowInstanceId);
		com.quark.risk.frtb.samr.results.model.Hierarchy h = new com.quark.risk.frtb.samr.results.model.Hierarchy();
		for (Hierarchy hierarchy : hData) {
			h.addHierarchyRow(hierarchy);
		}
		return h;
	}

	@RequestMapping(value = "/getAllSAMRWorkflowInstances", method = RequestMethod.GET)
	@ResponseBody
	public List<WorkflowInstance> getAllSAMRWorkflowInstances(HttpServletRequest req) throws ApplicationException {
		User u = getUser(req);
		List<WorkflowInstance> instances = workflowService
				.getWorkflowInstances(WorkflowNames.SAMR_RISK_CHARGE_CALC_WORKFLOW, u.getOrganization());
		for (WorkflowInstance inst : instances) {
			inst.setDate(DateUtil.getJavaDate(inst.getExcelDate()));
		}
		return instances;
	}

	@RequestMapping(value = "/intermediateResults", method = RequestMethod.POST)
	@ResponseBody
	public IntermediateResultsData getIntermediateResults(@RequestBody ResultsQueryForm query)
			throws ApplicationException {
		return samrDataService.getIntermediateResults(getExcelDate(query.getValueDate()), query.getWorkflowId(),
				query.getNodeId());
	}

	@RequestMapping(value = "/getSamrDashboardData", method = RequestMethod.GET)
	@ResponseBody
	public Nvd3DashboardData getSamrDashboardData(@RequestParam String valueDate, @RequestParam Long workflowId)
			throws ApplicationException {
		WorkflowInstance wfInst = workflowService.getWorkflowInstance(getExcelDate(valueDate), workflowId);
		Nvd3DashboardData dd = new Nvd3DashboardData();
		dd.setValueDate(getValueDate(valueDate));
		dd.setWorkflowInstance(wfInst);
		if (wfInst == null) {
			return dd;
		}
		dd.setRiskClassLevelResults(samrDataService.getRiskClassLevelResults(wfInst));
		return dd;
	}

	public String toJson(Object results) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = mapper.writeValueAsString(results);
		jsonInString = jsonInString.replaceAll("nodeName", "Name");
		return jsonInString;
	}

	@RequestMapping(value = "/tradeLevelRiskChargeResults", method = RequestMethod.GET)
	@ResponseBody
	public List<TradeLevelRiskChargeResults> getTradeLevelRiskChargeResults(@RequestParam String valueDate,
			@RequestParam Long workflowId) throws ApplicationException {
		List<TradeLevelRiskChargeResults> trLevelRcResults = new ArrayList<>();
		WorkflowInstance workflowInstance = getWorkflowInstance(valueDate, workflowId);
		List<PoResults> results = samrService.calculateTradeLevelRiskCharge(workflowInstance, false);
		Map<String, Double> details = new TreeMap<>();
		StringWriter sw = new StringWriter();
		NumberFormat formatter = new DecimalFormat("###,###,###,###.00");
		for (PoResults r : results) {
			TradeLevelRiskChargeResults tr = new TradeLevelRiskChargeResults();
			tr.setTotalRiskCharge(r.getTotalRiskCharge());
			tr.setTradeIdentifier(r.getTradeIdentifier());
			List<RiskClassLevelResults> rclResults = r.getRiskClassResults();
			details.clear();
			sw.getBuffer().setLength(0);
			for (RiskClassLevelResults rcl : rclResults) {
				String riskClass = rcl.getRiskClass().toString();
				if (RiskClass.IR == rcl.getRiskClass() || RiskClass.FX == rcl.getRiskClass()
						|| RiskClass.EQ == rcl.getRiskClass() || RiskClass.COMMODITY == rcl.getRiskClass()
						|| RiskClass.CREDIT_NS == rcl.getRiskClass()) {
					if (rcl.getDelta() > 0) {
						details.put(riskClass + " Delta", rcl.getDelta());
					}
					if (rcl.getVega() > 0) {
						details.put(riskClass + " Vega", rcl.getVega());
					}
					if (rcl.getCurvature() > 0) {
						details.put(riskClass + " Curvature", rcl.getCurvature());
					}
				} else if (RiskClass.CREDIT_S_NCTP == rcl.getRiskClass()) {
					if (rcl.getDelta() > 0) {
						details.put("Credit Sec Non CTP", rcl.getDelta());
					}
				} else if (RiskClass.CREDIT_S_CTP == rcl.getRiskClass()) {
					if (rcl.getDelta() > 0) {
						details.put("Credit Sec CTP", rcl.getDelta());
					}
				} else if (RiskClass.DEFAULT_RISK == rcl.getRiskClass()) {
					if (rcl.getDrcNonSec() > 0) {
						details.put("DRC Non Sec", rcl.getDrcNonSec());
					}
					if (rcl.getDrcSecNonCtp() > 0) {
						details.put("DRC Sec Non CTP", rcl.getDrcSecNonCtp());
					}
					if (rcl.getDrcSecCtp() > 0) {
						details.put("DRC Sec CTP", rcl.getDrcSecCtp());
					}
				} else if (RiskClass.RESIDUAL_RISK == rcl.getRiskClass()) {
					if (rcl.getResType1() > 0) {
						details.put("Residual Type 1", rcl.getResType1());
					}
					if (rcl.getResType2() > 0) {
						details.put("Residual Type 2", rcl.getResType2());
					}
				}
			}
			Iterator<String> ite = details.keySet().iterator();
			while (ite.hasNext()) {
				String key = ite.next();
				sw.append("\"");
				sw.append(key);
				sw.append("\"");
				sw.append("=");
				sw.append(formatter.format(details.get(key)));
				if (ite.hasNext()) {
					sw.append("; ");
				}
			}
			tr.setDetails(sw.toString());
			trLevelRcResults.add(tr);
		}
		return trLevelRcResults;
	}
}
