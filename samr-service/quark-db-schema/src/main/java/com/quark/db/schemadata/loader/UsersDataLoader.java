package com.quark.db.schemadata.loader;

import com.quark.common.dao.CoreDao;
import com.quark.core.security.Group;
import com.quark.core.security.Organization;
import com.quark.core.security.Permissions;
import com.quark.core.security.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UsersDataLoader {

	@Autowired
	private CoreDao coreDao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Organization setupOrg() {
		Organization ast = new Organization("Asterix", true);
		coreDao.save(ast);
		return ast;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupPermissions() {
		Permissions all = new Permissions("ANY", "ALL");
		coreDao.save(all);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Set<Group> setupGroups() {
		Group superG = new Group("SUPER", true, null, null);
		coreDao.save(superG);
		Set<Group> groups = new HashSet<Group>();
		groups.add(superG);
		return groups;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupUsers(Organization ast, Set<Group> groups) {
		User u1 = new User("root", "sigirisetti@gmail.com", "password", ast, true, groups);
		coreDao.save(u1);
	}
}
