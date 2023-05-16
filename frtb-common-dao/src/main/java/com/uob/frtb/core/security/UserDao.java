package com.uob.frtb.core.security;

import com.uob.frtb.core.exception.ApplicationException;

public interface UserDao {
	User getUser(String email) throws ApplicationException;
	Long getUserOrgId(String email) throws ApplicationException;
}
