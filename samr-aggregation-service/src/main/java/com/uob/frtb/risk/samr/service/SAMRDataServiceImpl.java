package com.uob.frtb.risk.samr.service;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.data.CoreDataService;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.security.SecurityService;
import com.uob.frtb.core.workflow.WorkflowService;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.samr.csv.model.SAMRData;
import com.uob.frtb.risk.samr.dao.SAMRDao;
import com.uob.frtb.risk.samr.model.*;
import com.uob.frtb.risk.samr.model.Hierarchy;
import com.uob.frtb.risk.samr.results.*;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SAMRDataServiceImpl implements SAMRDataService {

	private static final String ID_SEPERATOR = ":";

	@Value("${samr.file.upload.dir:c:/tmp}")
	private String fileUploadDir;

	@Value("${samr.base.currency:USD}")
	private String baseCurrency;

	@Autowired
	protected SecurityService secDao;

	@Autowired
	private SAMRDao samrDao;

	@Autowired
	private CoreDao coreDao;

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private WorkflowService workflowService;

	@Override
	@Transactional
	public List<SamrMarketQuote> getMarketQuotes(WorkflowInstance workflowInst) {
		return samrDao.getMarketQuotes(workflowInst.getId());
	}

	@Override
	@Transactional
	public List<EquityInfo> getEquityInfo(WorkflowInstance workflowInst) {
		return samrDao.getEquityInfo(workflowInst.getId());
	}

	@Override
	@Transactional
	public List<TradeSensitivity> getTradeSensitivities(WorkflowInstance workflowInst) {
		return samrDao.getTradeSensitivities(workflowInst.getId());
	}

	@Override
	@Transactional
	public List<Hierarchy> getHierarchy(String wfInstId) {
		return samrDao.getHierarchy(wfInstId);
	}

	@Override
	@Transactional
	public void clearData(WorkflowInstance wfInst) {
		coreDao.refresh(wfInst);
		samrDao.deleteTradeSensitivities(wfInst);
		samrDao.deleteDRCNonSec(wfInst);
		samrDao.deleteDRCSecNonCTP(wfInst);
		samrDao.deleteDRCSecCTP(wfInst);
		samrDao.deleteEquityInfo(wfInst);
		samrDao.deleteCreditIssuerInfo(wfInst);
		samrDao.deleteCommodityInfo(wfInst);
		samrDao.deleteHierarchy(wfInst);
		samrDao.deleteMarketQuote(wfInst);
		samrDao.deleteResiduals(wfInst);
		// samrDao.deleteIntermediateResults(wfInst);
		samrDao.deleteValidationMessages(wfInst);
		samrDao.deleteSAMRResults(wfInst);
		coreDao.delete(wfInst);
	}

	@Override
	@Transactional
	public IntermediateResultsData getIntermediateResults(Integer excelDate, Long workflowId, String nodeId) {
		String[] parts = nodeId.split(ID_SEPERATOR);
		int idx = 0;
		String po = parts[idx++];
		String riskClass = parts[idx++];
		String sensType = parts[idx++];
		String currency = null;
		if (parts.length > 2) {
			currency = parts[idx++];
		}
		String rateIndex = null;
		if (parts.length > 3) {
			rateIndex = parts[idx++];
		}
		WorkflowInstance inst = workflowService.getWorkflowInstance(excelDate, workflowId);
		String poResultsId = samrDao.getPoResultsId(inst.getId(), po);
		List<IntermediateResultEntity> intermResults = samrDao.getIntermediateResults(inst.getId(), poResultsId,
				riskClass, sensType, currency, rateIndex);
		List results = new ArrayList();
		for (IntermediateResultEntity iRes : intermResults) {
			results.add(iRes.getResults().data());
		}
		IntermediateResultsData itermResultsData = new IntermediateResultsData();
		if (!results.isEmpty()) {
			itermResultsData.setData(results);
			itermResultsData.setHeaders(intermResults.get(0).getResults().headers());
		}
		return itermResultsData;
	}

	@Override
	@Transactional
	public Map<String, List<SAMRData>> getValidationMessages(int excelDate, Long workflowId)
			throws ApplicationException {
		WorkflowInstance inst = workflowService.getWorkflowInstance(excelDate, workflowId);
		DetachedCriteria c = DetachedCriteria.forClass(SAMRValidationMessages.class);
		c.add(Restrictions.eq("workflowInstance.id", inst.getId()));
		SAMRValidationMessages vMsgs = coreDao.getUniqueResult(c);
		if (vMsgs.getMsgs() != null) {
			return SerializationUtils.deserialize(vMsgs.getMsgs());
		}
		return Collections.emptyMap();
	}

	@Override
	@Transactional
	public List<RiskClassLevelResults> getRiskClassLevelResults(WorkflowInstance wfInst) {
		SAMRResults samrResults = getSAMRResults(wfInst);
		return samrResults.getRiskClassResults();
	}

	private SAMRResults getSAMRResults(WorkflowInstance wfInst) {
		DetachedCriteria c = DetachedCriteria.forClass(SAMRResults.class)
				.add(Restrictions.eq("workflowInstance.id", wfInst.getId()));
		List<SAMRResults> samrResults = coreDao.get(c);
		return samrResults.get(0);
	}

	@Override
	@Transactional
	public List<DRCNonSec> getDRCNonSecData(WorkflowInstance workflowInst) {
		DetachedCriteria c = DetachedCriteria.forClass(DRCNonSec.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInst.getId()));
		return coreDataService.get(c);
	}

	@Override
	@Transactional
	public List<DRCSecNonCTP> getDRCSecNonCTPData(WorkflowInstance workflowInst) {
		DetachedCriteria c = DetachedCriteria.forClass(DRCSecNonCTP.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInst.getId()));
		return coreDataService.get(c);
	}

	@Override
	@Transactional
	public List<DRCSecCTP> getDRCSecCTPData(WorkflowInstance workflowInst) {
		DetachedCriteria c = DetachedCriteria.forClass(DRCSecCTP.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInst.getId()));
		return coreDataService.get(c);
	}

	@Override
	@Transactional
	public RiskChargeResults getRiskChargeResults(WorkflowInstance workflowInst) {
		DetachedCriteria c = DetachedCriteria.forClass(SAMRResults.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInst.getId()));
		List<SAMRResults> rs = coreDataService.get(c);
		if (rs.isEmpty()) {
			return null;
		}
		SAMRResults samrResults = rs.get(0);
		// log.info("Risk class level results {} for workflow instance {}",
		// samrResults.getRiskClassResults().size(), workflowInst.getId());
		// log.info("Risk hierarchy results {} for workflow instance {}",
		// samrResults.getHierarchyResults().size(), workflowInst.getId());
		RiskChargeResults results = new RiskChargeResults();
		List<Result> resultRows = new ArrayList<>();
		double total = 0;
		for (PoResults por : samrResults.getPoResults()) {
			RiskChargeResults r = getRiskChargeResults(por);
			Result row = new Result(null, por.getPo(), false, 0, 0, 0, r.getTotalRiskCharge(), 0, 0);
			for (Result sub : r.getResults()) {
				row.addChild(sub);
			}
			resultRows.add(row);
			total += r.getTotalRiskCharge();
		}
		results.setResults(resultRows);
		results.setTotalRiskCharge(total);
		return results;
	}

	@Override
	public RiskChargeResults getRiskChargeResults(PoResults poResults) {
		Stack<Result> currentStack = new Stack<>();
		List<Result> rootNodes = new ArrayList<>();
		for (RiskClassHierarchyResultRow row : poResults.getHierarchyResults()) {
			Result node = new Result(row.getParentKey(), row.getResultName(), false, row.getAmountLowCorr(),
					row.getAmountBaseLowCorr(), row.getAmount(), row.getAmountBase(), row.getAmountHighCorr(),
					row.getAmountBaseHighCorr());
			if (row.getParentKey() == null) {
				if (!currentStack.isEmpty()) {
					rootNodes.add(currentStack.firstElement());
					currentStack.clear();
				}
				currentStack.add(node);
			} else {
				if (currentStack.peek().getParentName() == null) {
					currentStack.peek().addChild(node);
					currentStack.push(node);
				} else if (currentStack.peek().getParentName().equals(node.getParentName())) {
					currentStack.pop();
					currentStack.peek().addChild(node);
					currentStack.push(node);
				} else {
					int pkLen = currentStack.peek().getParentName().split(ID_SEPERATOR).length;
					int rwLen = row.getParentKey().split(ID_SEPERATOR).length;
					if (pkLen < rwLen) {
						currentStack.peek().addChild(node);
						currentStack.push(node);
					} else {
						while (currentStack.peek().getParentName() != null
								&& currentStack.peek().getParentName().length() >= row.getParentKey().length()) {
							currentStack.pop();
						}
						currentStack.peek().addChild(node);
						currentStack.push(node);
					}
				}
			}
		}
		rootNodes.add(currentStack.firstElement());

		return new RiskChargeResults(poResults.getTotalRiskCharge(), poResults.getSamrResults().getCurrency(),
				rootNodes);
	}

	@Override
	public List<CreditIssuerInfo> getCreditIssuerInfo(WorkflowInstance workflowInstance) {
		DetachedCriteria c = DetachedCriteria.forClass(CreditIssuerInfo.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInstance.getId()));
		return coreDataService.get(c);
	}

	@Override
	public List<CommodityInfo> getCommodityInfo(WorkflowInstance workflowInstance) {
		DetachedCriteria c = DetachedCriteria.forClass(CommodityInfo.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInstance.getId()));
		return coreDataService.get(c);
	}

	@Override
	public List<Residuals> getResiduals(WorkflowInstance workflowInst) {
		DetachedCriteria c = DetachedCriteria.forClass(Residuals.class)
				.add(Restrictions.eq("workflowInstance.id", workflowInst.getId()));
		return coreDataService.get(c);
	}

	@Override
	@Transactional
	public CalcRequest buildCalcRequest(WorkflowInstance workflowInstance, DataFilter filter, boolean persist) {
		CalcRequest req = new CalcRequest();
		req.setWorkflowInstance(workflowInstance);
		if (filter == null) {
			req.setTradeSensitivities(samrDao.getTradeSensitivities(workflowInstance.getId()));
			req.setDrcNonSecData(samrDao.getDRCNonSecData(workflowInstance.getId()));
			req.setDrcSecNonCTPData(samrDao.getDRCSecNonCTPData(workflowInstance.getId()));
			req.setDrcSecCTPData(samrDao.getDRCSecCTPData(workflowInstance.getId()));
			req.setResiduals(samrDao.getResiduals(workflowInstance.getId()));
		} else {
			req.setTradeSensitivities(samrDao.getTradeSensitivities(workflowInstance.getId(), filter));
			req.setDrcNonSecData(samrDao.getDRCNonSecData(workflowInstance.getId(), filter));
			req.setDrcSecNonCTPData(samrDao.getDRCSecNonCTPData(workflowInstance.getId(), filter));
			req.setDrcSecCTPData(samrDao.getDRCSecCTPData(workflowInstance.getId(), filter));
			req.setResiduals(samrDao.getResiduals(workflowInstance.getId(), filter));
		}
		req.setBaseCurrency(baseCurrency);
		req.setQuotes(getMarketQuotes(workflowInstance));
		req.setEquityInfo(getEquityInfo(workflowInstance));
		req.setCreditIssuerInfo(getCreditIssuerInfo(workflowInstance));
		req.setCommodityInfo(getCommodityInfo(workflowInstance));
		req.setPersistResults(persist);
		return req;
	}
}
