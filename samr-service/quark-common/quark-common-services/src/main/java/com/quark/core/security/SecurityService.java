package com.quark.core.security;

import com.quark.core.exception.ApplicationException;

import java.util.List;

public interface SecurityService {
	List<User> loadAllUsers();

	User getUser(String email) throws ApplicationException;

	Long getUserOrgId(String email) throws ApplicationException;
}
