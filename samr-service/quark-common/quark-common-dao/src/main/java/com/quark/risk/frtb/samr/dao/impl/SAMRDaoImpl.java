package com.quark.risk.frtb.samr.dao.impl;

import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.frtb.samr.dao.SAMRDao;
import com.quark.risk.frtb.samr.model.*;
import com.quark.risk.frtb.samr.results.IntermediateResultEntity;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.SAMRResults;
import com.quark.risk.frtb.samr.results.SAMRValidationMessages;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.quark.risk.frtb.samr.model.HierarchyFilters.*;

@Repository("SAMRDao")
public class SAMRDaoImpl extends HibernateDaoSupport implements SAMRDao {

	@Autowired
	public SAMRDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<TradeSensitivity> getTradeSensitivities(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TradeSensitivity> sens = session.createCriteria(TradeSensitivity.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return sens;
	}

	@Override
	public List<DRCNonSec> getDRCNonSecData(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<DRCNonSec> sens = session.createCriteria(DRCNonSec.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return sens;
	}

	@Override
	public List<DRCSecNonCTP> getDRCSecNonCTPData(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<DRCSecNonCTP> sens = session.createCriteria(DRCSecNonCTP.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return sens;
	}

	@Override
	public List<DRCSecCTP> getDRCSecCTPData(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<DRCSecCTP> sens = session.createCriteria(DRCSecCTP.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return sens;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TradeSensitivity> getTradeSensitivities(String workflowId, DataFilter filter) {
		return getFilteredSAMRData(TradeSensitivity.class, workflowId, filter);
	}

	@Override
	public List<DRCNonSec> getDRCNonSecData(String workflowId, DataFilter filter) {
		return getFilteredSAMRData(DRCNonSec.class, workflowId, filter);
	}

	@Override
	public List<DRCSecNonCTP> getDRCSecNonCTPData(String workflowId, DataFilter filter) {
		return getFilteredSAMRData(DRCSecNonCTP.class, workflowId, filter);
	}

	@Override
	public List<DRCSecCTP> getDRCSecCTPData(String workflowId, DataFilter filter) {
		return getFilteredSAMRData(DRCSecCTP.class, workflowId, filter);
	}

	@Override
	public List<Residuals> getResiduals(String workflowId, DataFilter filter) {
		return getFilteredSAMRData(Residuals.class, workflowId, filter);
	}

	private <T extends BaseTradeEntity> List<T> getFilteredSAMRData(Class<T> clazz, String workflowId,
			DataFilter filter) {
		String hql = "from %s ts where ts.workflowInstance.id = :wfId "
				+ "and ts.tradeIdentifier in (select h.tradeIdentifier from Hierarchy h where h.po = :po ";

		hql = String.format(hql, clazz.getName());

		String fn = "";
		if (BOOK.equalsIgnoreCase(filter.getFilterName())) {
			fn = "book";
			hql += " and h.book in (:" + fn + ")";
		} else if (DESK.equalsIgnoreCase(filter.getFilterName())) {
			fn = "desk";
			hql += " and h.desk in (:" + fn + ")";
		} else if (LOCATION.equalsIgnoreCase(filter.getFilterName())) {
			fn = "location";
			hql += " and h.location in (:" + fn + ")";
		}

		hql += ")";

		Session session = getSessionFactory().getCurrentSession();
		Query q = session.createQuery(hql).setString("wfId", workflowId).setString("po", filter.getPo());
		if (StringUtils.isNotBlank(filter.getFilterName())) {
			String[] fvals = filter.getFilterValue().split(",");
			q.setParameterList(fn, Arrays.asList(fvals)).list();
		}
		return q.list();
	}

	@Override
	public List<SamrMarketQuote> getMarketQuotes(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<SamrMarketQuote> quotes = session.createCriteria(SamrMarketQuote.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return quotes;
	}

	@Override
	public List<Hierarchy> getHierarchy(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(Hierarchy.class).add(Restrictions.eq("workflowInstance.id", workflowId)).list();
	}

	@Override
	public void deleteTradeSensitivities(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(TradeSensitivity.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteDRCNonSec(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(DRCNonSec.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteDRCSecNonCTP(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(DRCSecNonCTP.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteDRCSecCTP(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(DRCSecCTP.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteResiduals(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(Residuals.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteEquityInfo(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(EquityInfo.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteHierarchy(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(Hierarchy.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteMarketQuote(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(SamrMarketQuote.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteIntermediateResults(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(IntermediateResultEntity.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteValidationMessages(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(SAMRValidationMessages.class.getName(), wfInst.getId());
	}

	@Override
	public void deleteSAMRResults(WorkflowInstance wfInst) {
		Session session = getSessionFactory().getCurrentSession();
		List<SAMRResults> results = session.createCriteria(SAMRResults.class)
				.add(Restrictions.eq("workflowInstance.id", wfInst.getId())).list();
		if (!results.isEmpty()) {
			for (SAMRResults r : results) {
				session.delete(r);
			}
		}
	}

	private void deleteSAMRResults(Session session, SAMRResults r, String type) {
		String hql = "delete from " + type + " where samrResults.id = :Id";
		session.createQuery(hql).setString("Id", r.getId()).executeUpdate();
	}

	private void deleteSAMRDataByWorkflowInstId(String entity, String id) {
		String hql = "delete from " + entity + " where workflowInstance.id = :wfInstId";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setString("wfInstId", id).executeUpdate();
	}

	@Override
	public String getPoResultsId(String workflowInstance, String po) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "select id from " + PoResults.class.getName()
				+ " where samrResults.workflowInstance.id = :wid and po = :po";
		Query q = session.createQuery(hql);
		q.setString("wid", workflowInstance);
		q.setString("po", po);
		List list = q.list();
		if (!list.isEmpty() && list.size() == 1) {
			return (String) list.get(0);
		}
		throw new IllegalStateException();
	}

	@Override
	public List<IntermediateResultEntity> getIntermediateResults(String workflowInstance, String po, String riskClass,
			String sensType, String currency, String rateIndex) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(IntermediateResultEntity.class).add(Restrictions.eq("poResults.id", po))
				.add(Restrictions.eq("riskClass", RiskClass.valueOf(riskClass)))
				.add(Restrictions.eq("sensitivityType", sensType));
		if (currency != null) {
			c.add(Restrictions.eq("currency", currency));
		}
		if (rateIndex != null) {
			c.add(Restrictions.eq("rateIndex", rateIndex));
		}
		return c.list();
	}

	@Override
	public List<EquityInfo> getEquityInfo(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<EquityInfo> eqInfo = session.createCriteria(EquityInfo.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return eqInfo;
	}

	@Override
	public List<Residuals> getResiduals(String workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Residuals> residuals = session.createCriteria(Residuals.class)
				.add(Restrictions.eq("workflowInstance.id", workflowId)).list();
		return residuals;
	}

	@Override
	public void deleteCreditIssuerInfo(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(CreditIssuerInfo.class.getName(), wfInst.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPo(String wfInstId) {
		String hql = "select distinct po from " + Hierarchy.class.getName()
				+ " h where h.workflowInstance.id = :wfInstId";
		Session session = getSessionFactory().getCurrentSession();
		return session.createQuery(hql).setString("wfInstId", wfInstId).list();
	}

	@Override
	public Map<String, List<String>> groupTradeIdsByPo(String wfInstId) {
		String hql = "select h.po, h.tradeIdentifier from " + Hierarchy.class.getName()
				+ " h where h.workflowInstance.id = :wfInstId";
		Session session = getSessionFactory().getCurrentSession();
		List<Object[]> list = session.createQuery(hql).setString("wfInstId", wfInstId).list();
		Map<String, List<String>> m = new TreeMap<>();
		for (Object[] arr : list) {
			String key = (String) arr[0];
			String val = (String) arr[1];
			if (m.containsKey(key)) {
				m.get(arr[0]).add(val);
			} else {
				m.put(key, new ArrayList<>());
				m.get(key).add(val);
			}
		}
		return m;
	}

	@Override
	public void deleteCommodityInfo(WorkflowInstance wfInst) {
		deleteSAMRDataByWorkflowInstId(CommodityInfo.class.getName(), wfInst.getId());
	}
}
