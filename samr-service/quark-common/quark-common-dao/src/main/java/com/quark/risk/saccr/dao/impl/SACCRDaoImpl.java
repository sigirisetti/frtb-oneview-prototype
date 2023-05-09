package com.quark.risk.saccr.dao.impl;

import com.quark.risk.saccr.dao.SACCRDao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class SACCRDaoImpl extends HibernateDaoSupport implements SACCRDao {

	@Autowired
	public SACCRDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
}
