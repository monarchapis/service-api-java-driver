package com.monarchapis.driver.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {
	private String allowOrigins;
	private String allowMethods;
	private String allowHeaders;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String origin = req.getHeader("Origin");
		boolean isOptionsMethod = "OPTIONS".equalsIgnoreCase(req.getMethod());

		if (origin == null) {
			// Return standard response if OPTIONS request w/o Origin header
			if (isOptionsMethod) {
				res.setHeader("Allow", allowMethods);
				res.setStatus(200);
				return;
			}
		} else {
			// Some browsers don't like * with allowCredentials=true so just
			// echo back the Origin.
			res.setHeader("Access-Control-Allow-Origin", allowOrigins.equals("*") ? origin : allowOrigins);
			res.setHeader("Access-Control-Allow-Methods", allowMethods);
			res.setHeader("Access-Control-Allow-Headers", allowHeaders);
			res.setHeader("Access-Control-Max-Age", "1728000");
			res.addHeader("Access-Control-Allow-Credentials", "true");
		}

		// Pass request down the chain, except for OPTIONS
		if (!isOptionsMethod) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		allowOrigins = getInitParameter(config, "allowOrigins", "*");
		allowMethods = getInitParameter(config, "allowMethods", "GET, POST, HEAD, OPTIONS, DELETE, PUT, PATCH");
		allowHeaders = getInitParameter(
				config,
				"allowHeaders",
				"Content-Type, Accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers, Api-Key, X-Api-Key, Authorization");
	}

	private static String getInitParameter(FilterConfig config, String name, String defaultValue) {
		String value = config.getInitParameter(name);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}
}
