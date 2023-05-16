package com.uob.frtb.core.workflow;

import com.uob.frtb.core.workflow.WorkflowDao;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class WorkflowDaoImpl extends HibernateDaoSupport implements WorkflowDao {

	@Autowired
	public WorkflowDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public void deleteWorkflowInstance(int excelDate, Long workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		WorkflowInstance inst = getWorkflowInstance(excelDate, workflowId);
		if (inst != null) {
			session.delete(inst);
		}
	}

	public WorkflowInstance getWorkflowInstance(int excelDate, Long workflowId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(WorkflowInstance.class);
		c.add(Restrictions.eq("excelDate", excelDate));
		c.add(Restrictions.eq("workflow.id", workflowId));
		c.setMaxResults(1);
		List<WorkflowInstance> results = c.list();
		if (results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

	@Override
	public <T extends Serializable> List<T> getWorkflowData(Class<T> c, WorkflowInstance wfInst) {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(c).add(Restrictions.eq("workflowInstance.id", wfInst.getId())).list();
	}

	@Override
	public void deleteWorkflowData(Class<?> c, WorkflowInstance wfInst) {
		deleteWorkflowData(c.getName(), wfInst.getId());
	}

	private void deleteWorkflowData(String entity, String id) {
		String hql = "delete from " + entity + " where workflowInstance.id = :wfInstId";
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery(hql).setString("wfInstId", id).executeUpdate();
	}
}
