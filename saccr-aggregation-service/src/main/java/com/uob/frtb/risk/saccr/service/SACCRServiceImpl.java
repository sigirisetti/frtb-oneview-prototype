package com.uob.frtb.risk.saccr.service;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.workflow.WorkflowDao;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.saccr.model.Collateral;
import com.uob.frtb.risk.saccr.model.SaccrMarketQuote;
import com.uob.frtb.risk.saccr.model.RequiredTradeAttributes;
import com.uob.frtb.risk.saccr.model.SupervisoryParameters;
import com.uob.frtb.risk.saccr.model.Trade;
import com.uob.frtb.risk.saccr.results.SACCRResults;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SACCRServiceImpl implements SACCRService {

	@Value("${samr.base.currency:SEK}")
	private String baseCurrency;

	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private CoreDao coreDao;

	@Override
	public SACCRResults calculate(WorkflowInstance workflowInst) {

		SACCRCalculator calc = appCtx.getBean(SACCRCalculator.class);

		List<SaccrMarketQuote> marketData = workflowDao.getWorkflowData(SaccrMarketQuote.class, workflowInst);
		calc.setMarketData(marketData);
		List<Collateral> collateral = workflowDao.getWorkflowData(Collateral.class, workflowInst);
		calc.setCollateral(collateral);
		List<RequiredTradeAttributes> reqTradeAttributes = coreDao.loadAll(RequiredTradeAttributes.class);
		calc.setReqTradeAttributes(reqTradeAttributes);
		List<Trade> trades = workflowDao.getWorkflowData(Trade.class, workflowInst);
		Collections.sort(trades, new Comparator<Trade>() {
			@Override
			public int compare(Trade t1, Trade t2) {
				return t1.getSeq().compareTo(t2.getSeq());
			}
		});
		calc.setTrades(trades);
		List<SupervisoryParameters> supParams = coreDao.loadAll(SupervisoryParameters.class);
		calc.setSupervisoryParameters(supParams);

		calc.setBaseCurrency(baseCurrency);
		calc.setValueDate(DateUtil.getJavaDate(workflowInst.getExcelDate()));

		return calc.calculate();
	}
}
