package com.quark.core.security.impl;

import com.quark.common.dao.impl.CoreDaoImpl;
import com.quark.core.exception.ApplicationException;
import com.quark.core.security.User;
import com.quark.core.security.UserDao;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("UserDao")
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	@Autowired
	private CoreDaoImpl coreDao;

	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public User getUser(String email) throws ApplicationException {
		DetachedCriteria query = DetachedCriteria.forClass(User.class).add(Property.forName("email").eq(email));
		User user = coreDao.getUniqueResult(query);
		Hibernate.initialize(user);
		return user;
	}

	@Override
	public Long getUserOrgId(String email) throws ApplicationException {
		return getUser(email).getOrganization().getId();
	}
}
