/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

/**
 * A servlet filter that handles setting the appropriate CORS headers for API
 * requests.
 * 
 * @author Phil Kedy
 */
public class CorsFilter implements Filter {
	/**
	 * The allow origins header setting.
	 */
	private String allowOrigins;

	/**
	 * The allow methods header setting.
	 */
	private String allowMethods;

	/**
	 * The allow headers header setting.
	 */
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
