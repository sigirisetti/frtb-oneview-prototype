package com.uob.frtb.web.core.controller;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.security.Organization;
import com.uob.frtb.core.security.SecurityService;
import com.uob.frtb.core.security.User;
import com.uob.frtb.core.workflow.WorkflowTriggerHelper;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;

/**
 * Base class for all controllers. Provides some utility methods and data for
 * all
 * 
 * @author Surya
 */
public abstract class BaseController {

	protected static final String CLAIMS = "claims";

	private static final String YYYY_MM_DD_T_HH_MM_SS_S_Z = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	protected final Map<String, User> userDb = new HashMap<>();

	@Autowired
	protected SecurityService secDao;
	
	@Autowired
	protected ApplicationContext appCtx;

	@Autowired
	protected WorkflowTriggerHelper workflowTrigger;

	protected void init() {
		if (!userDb.isEmpty()) {
			return;
		}
		List<User> all = secDao.loadAllUsers();
		for (User u : all) {
			userDb.put(u.getEmail(), u);
		}
	}

	protected User getUser(HttpServletRequest request) throws ApplicationException {
		Claims claims = (Claims) request.getAttribute(CLAIMS);
		return getUser(claims.getSubject());
	}

	protected User getUser(String email) throws ApplicationException {
		init();
		return secDao.getUser(email);
	}

	protected Organization getOrganization(HttpServletRequest request) throws ApplicationException {
		return getUser(request).getOrganization();
	}

	protected Date getValueDate(String valueDate) throws ApplicationException {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_S_Z);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return sdf.parse(valueDate);
		} catch (ParseException e) {
			try {
				LocalDate d = LocalDate.parse(valueDate);
				return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}catch(Exception e2) {
				throw new ApplicationException(-1, String.format("Expected datetime format '%s'. Got datetime value : %s",
						YYYY_MM_DD_T_HH_MM_SS_S_Z, valueDate));
			}
		}
	}
	
	protected int getExcelDate(String valueDate) throws ApplicationException {
		return (int) DateUtil.getExcelDate(getValueDate(valueDate));
	}
}
