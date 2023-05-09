package com.quark.web.core.config.security.controller;

import com.quark.core.data.CoreDataService;
import com.quark.core.security.Group;
import com.quark.core.security.Organization;
import com.quark.core.security.User;
import com.quark.security.random.RandomString;
import com.quark.web.core.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services/security")
public class SecuritySetupController extends BaseController {

	@Autowired
	private CoreDataService coreDataService;

	@RequestMapping(value = "/saveOrg", method = RequestMethod.POST)
	public Organization saveOrg(@RequestBody final Organization org) {
		return coreDataService.saveOrUpdate(org);
	}

	@RequestMapping(value = "/listOrg", method = RequestMethod.GET)
	public List<Organization> listOrg() {
		return coreDataService.loadAll(Organization.class);
	}

	@RequestMapping(value = "/deleteOrg", method = RequestMethod.POST)
	public Organization deleteOrg(@RequestBody final Organization org) {
		coreDataService.delete(org);
		return org;
	}

	@RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
	public Group saveGroup(@RequestBody final Group grp) {
		return coreDataService.saveOrUpdate(grp);
	}

	@RequestMapping(value = "/listGroups", method = RequestMethod.GET)
	public List<Group> listGroups() {
		return coreDataService.loadAll(Group.class);
	}

	@RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
	public Group deleteGroup(@RequestBody final Group grp) {
		coreDataService.delete(grp);
		return grp;
	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public User registerUser(@RequestBody final User user) {
		RandomString r = new RandomString(8);
		user.setPassword(r.nextString());
		return coreDataService.save(user);
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public User saveUser(@RequestBody final User user) {
		return coreDataService.saveOrUpdate(user);
	}

	@RequestMapping(value = "/listUsers", method = RequestMethod.GET)
	public List<User> listUsers() {
		return coreDataService.loadAll(User.class);
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public User deleteUser(@RequestBody final User user) {
		coreDataService.delete(user);
		return user;
	}
}
