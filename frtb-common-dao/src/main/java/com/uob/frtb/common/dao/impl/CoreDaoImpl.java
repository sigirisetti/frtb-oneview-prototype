package com.uob.frtb.common.dao.impl;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * All services should use this generic dao for o/r persistence unless this
 * doesn't provide required API
 * 
 * @author Surya
 *
 */
@Repository("CoreDao")
public class CoreDaoImpl extends HibernateDaoSupport implements CoreDao {

	@Autowired
	public CoreDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public <T> T save(T ser) {
		Session session = getSessionFactory().getCurrentSession();
		session.save(ser);
		return ser;
	}

	@Override
	public <T> List<T> save(List<T> list) {
		Session session = getSessionFactory().getCurrentSession();
		for (T ser : list) {
			session.save(ser);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> loadAll(Class<T> type) {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(type).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> get(DetachedCriteria criteria) {
		Session session = getSessionFactory().getCurrentSession();
		return criteria.getExecutableCriteria(session).list();
	}

	@Override
	public <T> T getFirstResult(DetachedCriteria criteria) {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<T> results = criteria.getExecutableCriteria(session).setMaxResults(1).list();
		if (!results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	@Override
	public <T> T getUniqueResult(DetachedCriteria criteria) throws ApplicationException {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<T> results = criteria.getExecutableCriteria(session).setMaxResults(1).list();
		if (!results.isEmpty() && results.size() == 1) {
			return results.get(0);
		}
		throw new ApplicationException(-1, "Non unique result retreived for criteria");
	}

	@Override
	public <T> void delete(T obj) {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(obj);
	}

	@Override
	public <T> void delete(List<T> list) {
		Session session = getSessionFactory().getCurrentSession();
		for (T ser : list) {
			session.delete(ser);
		}
	}

	@Override
	public <T> T saveOrUpdate(T ser) {
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(ser);
		return ser;
	}

	@Override
	public <T> List<T> saveOrUpdate(List<T> list) {
		Session session = getSessionFactory().getCurrentSession();
		for (T obj : list) {
			session.saveOrUpdate(obj);
		}
		return list;
	}

	@Override
	public void refresh(WorkflowInstance wfInst) {
		Session session = getSessionFactory().getCurrentSession();
		session.refresh(wfInst);
	}
}
