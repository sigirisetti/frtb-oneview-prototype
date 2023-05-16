package com.uob.frtb.risk.frtb.samr.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.security.SecurityService;
import com.uob.frtb.core.security.User;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.frtb.samr.calculators.SAMRCalculator;
import com.uob.frtb.risk.frtb.samr.dao.SAMRDao;
import com.uob.frtb.risk.frtb.samr.model.*;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.SAMRResults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SAMRRiskChargeCalculationServiceImpl implements SAMRRiskChargeCalculationService {

	@Value("${samr.file.upload.dir:c:/tmp}")
	private String fileUploadDir;

	@Value("${samr.base.currency:USD}")
	private String baseCurrency;

	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	protected SecurityService secDao;

	@Autowired
	private SAMRDao samrDao;

	@Autowired
	private CoreDao coreDao;

	@Autowired
	private SAMRDataService samrDataService;
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	@Transactional
	public SAMRResults calculateMargins(WorkflowInstance workflowInst, boolean persist) throws ApplicationException {

		//TODO: cache results?
		IMap<Integer, String> map = hazelcastInstance.getMap("DataMap");
		map.put(1, "Joe");
		map.put(2, "Ali");
		map.put(3, "Avi");

		SAMRCalculator samrCalc = appCtx.getBean(SAMRCalculator.class);
		Map<String, List<String>> poMap = samrDao.groupTradeIdsByPo(workflowInst.getId());
		SAMRResults samrResults = new SAMRResults();
		samrResults.setWorkflowInstance(workflowInst);
		samrResults.setCurrency(baseCurrency);
		List<PoResults> poResultList = new ArrayList<>();
		CalcRequest fullReq = samrDataService.buildCalcRequest(workflowInst, null, persist);
		for (String po : poMap.keySet()) {
			log.info("Calculating risk charge for PO : {}", po);
			PoResults poResults = new PoResults();
			poResults.setPo(po);
			CalcRequest req = fullReq.filterDataByTradeIds(poMap.get(po), fullReq);
			poResults.setSamrResults(samrResults);
			req.validate();
			samrCalc.setCalcRequest(req);
			samrCalc.calculate(poResults);
			poResultList.add(poResults);
		}

		PoResults noPoResults = new PoResults();
		noPoResults.setPo("NO_PO");
		CalcRequest req = fullReq.filterOrphenedTrades(poMap, fullReq);
		noPoResults.setSamrResults(samrResults);
		try {
			req.validate();
			samrCalc.setCalcRequest(req);
			samrCalc.calculate(noPoResults);
			poResultList.add(noPoResults);
		} catch (Exception e) {
			log.info("No Orphaned trades : {}", e.getMessage());
		}

		samrResults.setPoResults(poResultList);
		if (persist) {
			coreDao.save(samrResults);
		}
		return samrResults;
	}

	@Override
	@Transactional
	public PoResults calculateMarginsApplyingFilter(WorkflowInstance workflowInstance, DataFilter filter)
			throws ApplicationException {
		SAMRCalculator samrCalc = appCtx.getBean(SAMRCalculator.class);
		CalcRequest req = samrDataService.buildCalcRequest(workflowInstance, filter, false);
		req.validate();
		samrCalc.setCalcRequest(req);
		PoResults poResults = new PoResults();
		SAMRResults samrResults = new SAMRResults();
		samrResults.setCurrency(baseCurrency);
		poResults.setSamrResults(samrResults);
		samrCalc.calculate(poResults);
		return poResults;
	}

	@Override
	public File getFileUploadDir(int excelDate, Long workflowId, String email) throws ApplicationException {
		String target = fileUploadDir;
		if (StringUtils.isBlank(target)) {
			String tmpDir = System.getProperty("java.io.tmpdir");
			if (!tmpDir.endsWith("/")) {
				tmpDir = tmpDir.concat("/");
			}
			target = tmpDir.concat("asterix/uploads/samr");
		}
		if (!target.endsWith("/")) {
			target = target + "/";
		}
		User u = secDao.getUser(email);
		target = target.concat(String.valueOf(u.getOrganization().getId())).concat("/");
		target = target.concat(String.valueOf(excelDate)).concat("/").concat(String.valueOf(workflowId));
		File f = new File(target);
		if (!f.exists()) {
			boolean created = f.mkdirs();
			if (!created) {
				log.error("Unable to create file upload dir : " + target);
				return null;
			}
		}
		return f;
	}

	@Override
	@Transactional
	public List<PoResults> calculateTradeLevelRiskCharge(WorkflowInstance workflowInstance, boolean persist)
			throws ApplicationException {

		log.info("Calculating trade level risk charge for value date : {}, workflow : {}", workflowInstance.getDate(),
				workflowInstance.getWorkflow().getName());
		SAMRCalculator samrCalc = appCtx.getBean(SAMRCalculator.class);
		// Trade level data
		List<TradeSensitivity> sens = samrDao.getTradeSensitivities(workflowInstance.getId());
		Map<String, List<TradeSensitivity>> groupedSens = sens.stream()
				.collect(Collectors.groupingBy(TradeSensitivity::getTradeIdentifier));
		List<DRCNonSec> drcNonSec = samrDao.getDRCNonSecData(workflowInstance.getId());
		Map<String, List<DRCNonSec>> groupedDrcNonSec = drcNonSec.stream()
				.collect(Collectors.groupingBy(DRCNonSec::getTradeIdentifier));
		List<DRCSecNonCTP> drcSecNonCtp = samrDao.getDRCSecNonCTPData(workflowInstance.getId());
		Map<String, List<DRCSecNonCTP>> groupedDrcSecNonCtp = drcSecNonCtp.stream()
				.collect(Collectors.groupingBy(DRCSecNonCTP::getTradeIdentifier));
		List<DRCSecCTP> drcSecCtp = samrDao.getDRCSecCTPData(workflowInstance.getId());
		Map<String, List<DRCSecCTP>> groupedDrcSecCtp = drcSecCtp.stream()
				.collect(Collectors.groupingBy(DRCSecCTP::getTradeIdentifier));
		List<Residuals> residuals = samrDao.getResiduals(workflowInstance.getId());
		Map<String, Residuals> mappedResiduals = residuals.stream()
				.collect(Collectors.toMap(Residuals::getTradeIdentifier, Function.identity(), (v1, v2) -> v2));
		// Other data
		List<SamrMarketQuote> marketData = samrDataService.getMarketQuotes(workflowInstance);
		List<EquityInfo> equityInfo = samrDataService.getEquityInfo(workflowInstance);

		Set<String> allTradeIds = new HashSet<>();
		allTradeIds.addAll(groupedSens.keySet());
		allTradeIds.addAll(groupedDrcNonSec.keySet());
		allTradeIds.addAll(groupedDrcSecNonCtp.keySet());
		allTradeIds.addAll(groupedDrcSecCtp.keySet());
		allTradeIds.addAll(mappedResiduals.keySet());

		log.info("Total number of trades : {}", allTradeIds.size());

		SAMRResults samrResults = new SAMRResults();
		samrResults.setCurrency(baseCurrency);

		List<PoResults> tradeResults = new ArrayList<>();
		for (String id : allTradeIds) {
			CalcRequest req = new CalcRequest();
			req.setWorkflowInstance(workflowInstance);
			if (groupedSens.containsKey(id)) {
				req.setTradeSensitivities(groupedSens.get(id));
			}
			if (groupedDrcNonSec.containsKey(id)) {
				req.setDrcNonSecData(groupedDrcNonSec.get(id));
			}
			if (groupedDrcSecNonCtp.containsKey(id)) {
				req.setDrcSecNonCTPData(groupedDrcSecNonCtp.get(id));
			}
			if (groupedDrcSecCtp.containsKey(id)) {
				req.setDrcSecCTPData(groupedDrcSecCtp.get(id));
			}
			if (mappedResiduals.containsKey(id)) {
				req.setResiduals(Arrays.asList(mappedResiduals.get(id)));
			}
			req.setBaseCurrency(baseCurrency);
			req.setQuotes(marketData);
			req.setEquityInfo(equityInfo);
			req.setPersistResults(persist);
			req.validate();
			samrCalc.setCalcRequest(req);
			PoResults poResults = new PoResults();
			poResults.setSamrResults(samrResults);
			samrCalc.calculate(poResults);
			poResults.setTradeIdentifier(id);
			tradeResults.add(poResults);
		}
		log.info("Total number of trades results : {}", tradeResults.size());
		Collections.sort(tradeResults, (o1, o2) -> -Double.compare(o1.getTotalRiskCharge(), o2.getTotalRiskCharge()));
		return tradeResults;
	}
}
