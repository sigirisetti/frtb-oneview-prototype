package com.quark.core.security;

import com.quark.core.exception.ApplicationException;

public interface UserDao {
	User getUser(String email) throws ApplicationException;
	Long getUserOrgId(String email) throws ApplicationException;
}
