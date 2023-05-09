package com.quark.risk.saccr.service;

import com.quark.common.dao.CoreDao;
import com.quark.core.workflow.WorkflowDao;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.saccr.model.Collateral;
import com.quark.risk.saccr.model.SaccrMarketQuote;
import com.quark.risk.saccr.model.Trade;
import com.quark.risk.saccr.results.SACCRValidationMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SACCRDataServiceImpl implements SACCRDataService {

	@Autowired
	private CoreDao coreDao;

	@Autowired
	private WorkflowDao workflowDao;

	@Override
	@Transactional
	public void clearData(WorkflowInstance wfInst) {
		coreDao.refresh(wfInst);
		workflowDao.deleteWorkflowData(SACCRValidationMessages.class, wfInst);
		workflowDao.deleteWorkflowData(SaccrMarketQuote.class, wfInst);
		workflowDao.deleteWorkflowData(Collateral.class, wfInst);
		workflowDao.deleteWorkflowData(Trade.class, wfInst);
		coreDao.delete(wfInst);
	}
}
