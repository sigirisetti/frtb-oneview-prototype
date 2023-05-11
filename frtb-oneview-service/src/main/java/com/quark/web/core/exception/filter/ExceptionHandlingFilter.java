package com.quark.web.core.exception.filter;

import com.quark.core.exception.ApplicationException;

import org.springframework.web.util.NestedServletException;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Top level exception handling filter. Extracts error code and send meaningful message to user
 * 
 * @author Surya
 *
 */
@Slf4j
public class ExceptionHandlingFilter implements Filter {

	private static final String UNHANDLED_EX = "Error happened during request processing. Refer to server logs";

	@Override
    public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {
		try {
			chain.doFilter(request, response);
		}catch(NestedServletException e) {
			log.error(e.getMessage(), e);
			if (e.getCause() instanceof ApplicationException) {
				writeErrorResponse(response, (ApplicationException) e.getCause());
			} else if (e.getCause() != null) {
				writeErrorResponse(response, e.getCause().getMessage());
			} else {
				writeErrorResponse(response, UNHANDLED_EX);
			}
		} catch (Exception t) {
			log.error(t.getMessage(), t);
			if (t.getCause() != null) {
				writeErrorResponse(response, t.getCause().getMessage());
			} else {
				writeErrorResponse(response, UNHANDLED_EX);
			}
		}
	}

	private void writeErrorResponse(ServletResponse response, ApplicationException ae) throws IOException {
		((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(String.format("{\"message\" : \"%s\"}", ae.getMessage()));
	}

	private void writeErrorResponse(ServletResponse response, String msg) throws IOException {
		((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(String.format("{\"message\" : \"%s\"}", msg));
	}

	@Override
	public void destroy() {
	}
}
