package com.uob.frtb.core.security;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("SecurityService")
@Transactional
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CoreDao coreDao;

	@Override
	public List<User> loadAllUsers() {
		List<User> users = coreDao.loadAll(User.class);
		Hibernate.initialize(users);
		/*
		for (User u : users) {
			Hibernate.initialize(u);
			Hibernate.initialize(u.getGroups());
		}
		*/
		return users;
	}

	@Override
	public Long getUserOrgId(String email) throws ApplicationException {
		return userDao.getUserOrgId(email);
	}

	@Override
	public User getUser(String email) throws ApplicationException {
		return userDao.getUser(email);
	}
}
