package com.quark.web.core.auth.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();
        log.info("Path == {}", path);
        if (path == null
                || path.endsWith("/h2-console")
                || path.endsWith(".ico")
                || path.endsWith(".js")
                || path.endsWith(".css")
                || path.endsWith("/index.html")
                || path.endsWith("/services/login")
                || path.endsWith("swagger-ui.html")
                || path.contains("/webjars/springfox-swagger-ui/")
                || path.contains("/swagger-resources")
                || path.endsWith("/csrf")
                || path.endsWith("/name")
                || path.endsWith("/v2/api-docs")
                ) {
            chain.doFilter(request, response);
        } else {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header.");
            }
            final String token = authHeader.substring(7);
            try {
                final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
            } catch (final SignatureException e) {
                throw new ServletException("Invalid token.");
            }
            chain.doFilter(req, res);
        }
    }
}
