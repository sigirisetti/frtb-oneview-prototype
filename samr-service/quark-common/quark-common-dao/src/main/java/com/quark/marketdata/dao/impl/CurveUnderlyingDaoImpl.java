package com.quark.marketdata.dao.impl;

import com.quark.marketdata.curve.CurveUnderlyingMoneyMarket;
import com.quark.marketdata.curve.CurveUnderlyingSwap;
import com.quark.marketdata.dao.CurveUnderlyingDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CurveUnderlyingDao")
public class CurveUnderlyingDaoImpl extends HibernateDaoSupport implements CurveUnderlyingDao {

	@Autowired
	public CurveUnderlyingDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public void save(CurveUnderlyingMoneyMarket cu) {
		getHibernateTemplate().save(cu);
	}

	@Override
	public List<CurveUnderlyingMoneyMarket> listCurveUnderlyingMoneyMarket() {

		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<CurveUnderlyingMoneyMarket> underlyings = session.createCriteria(CurveUnderlyingMoneyMarket.class).list();
		return underlyings;
	}

	@Override
	public void save(CurveUnderlyingSwap curveUnderlyingSwap) {
		Session session = getSessionFactory().getCurrentSession();
		session.persist(curveUnderlyingSwap);
	}

	@Override
	public List<CurveUnderlyingSwap> listCurveUnderlyingSwap() {

		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<CurveUnderlyingSwap> underlyings = session.createCriteria(CurveUnderlyingSwap.class).list();
		return underlyings;
	}
}
