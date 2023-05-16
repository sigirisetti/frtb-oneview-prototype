package com.uob.frtb.web.core.auth.controller;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.core.security.User;
import com.uob.frtb.web.core.controller.BaseController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping(value = "/services")
public class AuthenticationController extends BaseController {

	@RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login, HttpServletResponse res) {
		init();
		User user = userDb.get(login.username);
		if (user == null || !user.getPassword().equals(login.password)) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return new LoginResponse(null, false, "Invalid email or password");
		}
		if (!user.isActive()) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return new LoginResponse(null, false, "Your account is de-activated");
		}
		return new LoginResponse(
				Jwts.builder().setSubject(login.username).claim("roles", userDb.get(login.username).getGroups())
						.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact(),
				true, "");
	}

	@RequestMapping(value = "getLoginUser", method = RequestMethod.GET)
    public User getLoginUser(HttpServletRequest request) throws ApplicationException {
		return getUser(request);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	private static class UserLogin {
		private String username;
		private String password;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	private static class LoginResponse {
		private String token;
		private boolean success;
		private String message;
	}
}
