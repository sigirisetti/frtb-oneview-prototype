package com.uob.frtb.core.security;

import com.uob.frtb.core.exception.ApplicationException;

import java.util.List;

public interface SecurityService {
	List<User> loadAllUsers();

	User getUser(String email) throws ApplicationException;

	Long getUserOrgId(String email) throws ApplicationException;
}
