package com.uob.frtb.marketdata.dao;

import com.uob.frtb.marketdata.dao.ReferenceDataDao;
import com.uob.frtb.refdata.model.NameValuePair;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ReferenceDataDao")
public class ReferenceDataDaoImpl extends HibernateDaoSupport implements ReferenceDataDao {

	@Autowired
	public ReferenceDataDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<NameValuePair> getAllRefTypes() {
		Session session = getSessionFactory().getCurrentSession();
		return session.createCriteria(NameValuePair.class).addOrder(Order.asc("refType")).addOrder(Order.asc("seq"))
				.list();
	}

	@Override
	public List<String> getNamesAsList(String refType) {
		Session session = getSessionFactory().getCurrentSession();
		return session.createQuery("select name from NameValuePair n where n.refType = :refType order by n.seq")
				.setString("refType", refType).list();
	}

	@Override
	public List<NameValuePair> getRefType(String refType) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(NameValuePair.class);
		c.add(Restrictions.eq("refType", refType));
		List<NameValuePair> list = c.list();
		return list;
	}

	public void delete(String refType) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "delete from NameValuePair where refType= :refType";
		session.createQuery(hql).setString("refType", refType).executeUpdate();
	}

	@Override
	public void save(String refType, List<NameValuePair> l) {
		delete(refType);
		Session session = getSessionFactory().getCurrentSession();
		for (NameValuePair n : l) {
			session.save(n);
		}
	}

	@Override
	public void delete(List<String> refTypes) {
		Session session = getSessionFactory().getCurrentSession();
		for (String refType : refTypes) {
			String hql = "delete from NameValuePair where refType= :refType";
			session.createQuery(hql).setString("refType", refType).executeUpdate();
		}
	}
}
