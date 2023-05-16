package com.uob.frtb.risk.saccr.service;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.workflow.WorkflowDao;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.saccr.model.Collateral;
import com.uob.frtb.risk.saccr.model.SaccrMarketQuote;
import com.uob.frtb.risk.saccr.model.Trade;
import com.uob.frtb.risk.saccr.results.SACCRValidationMessages;

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
