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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationSettings;
import com.monarchapis.driver.util.ServiceResolver;

/**
 * Wrappers a HttpServiceRequest and provides additional methods to support
 * various authentication schemes such as Hawk.
 * 
 * @author Phil Kedy
 */
public class ApiRequest extends HttpServletRequestWrapper {
	/**
	 * The current API request for the thread.
	 */
	private static InheritableThreadLocal<ApiRequest> current = new InheritableThreadLocal<ApiRequest>();

	/**
	 * The request body.
	 */
	private byte[] body;

	/**
	 * The map of request headers.
	 */
	private Map<String, List<String>> headers;

	/**
	 * The auto-generated request identifier.
	 */
	private String requestId;

	public ApiRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.body = IOUtils.toByteArray(request.getInputStream());
		requestId = StringUtils.replace(UUID.randomUUID().toString(), "-", "");
	}

	/**
	 * Returns the current API request for the current thread.
	 * 
	 * @return the current API request.
	 */
	public static ApiRequest getCurrent() {
		return current.get();
	}

	/**
	 * Sets the current API request for the current thread.
	 * 
	 * @param request
	 *            The API request to set.
	 */
	public static void setCurrent(ApiRequest request) {
		if (request != null) {
			current.set(request);
		} else {
			current.remove();
		}
	}

	public static void remove() {
		current.remove();
	}

	public String getRequestId() {
		return requestId;
	}

	public ServletInputStream getInputStream() {
		return new ServletInputStreamWrapper(body);
	}

	public byte[] getBody() {
		return body;
	}

	/**
	 * Utility method to calculate the full URL.
	 * 
	 * @return The full URL as a string.
	 */
	public String getFullURL() {
		StringBuffer requestURL = getRequestURL();
		String queryString = getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	/**
	 * Returns the request headers are a convenient map.
	 * 
	 * @return The request headers as a map.
	 */
	public Map<String, List<String>> getHeaderMap() {
		if (this.headers == null) {
			Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
			Enumeration<String> e = getHeaderNames();

			while (e.hasMoreElements()) {
				String headerName = e.nextElement();
				ArrayList<String> values = new ArrayList<String>();
				Enumeration<String> e2 = getHeaders(headerName);

				while (e2.hasMoreElements()) {
					values.add(e2.nextElement());
				}

				headers.put(headerName, values);
			}

			this.headers = Collections.unmodifiableMap(headers);
		}

		return this.headers;
	}

	/**
	 * Returns the true client IP address. If the request went through a reverse
	 * proxy, then the X-Forwarded-For header is used to determine the client IP
	 * address. Otherwise, the remote address of the request is used.
	 * 
	 * @return The IP address as a string.
	 */
	public String getIpAddress() {
		String ipAddress = StringUtils.trimToNull(getHeader("X-Forwarded-For"));

		// If there is an X-Forwarded-For header, pull the first IP in the comma
		// separated list.
		return (ipAddress != null) ? StringUtils.split(ipAddress, ',')[0].trim() : getRemoteAddr();
	}

	/**
	 * Creates an authentication request based on the request.
	 * 
	 * @return the created <code>AuthenticationRequest</code> object.
	 */
	public AuthenticationRequest createAuthorizationRequest() {
		AuthenticationRequest auth = new AuthenticationRequest();
		AuthenticationSettings settings = ServiceResolver.getInstance().required(AuthenticationSettings.class);

		auth.setProtocol(getProtocol());
		auth.setMethod(getMethod());
		auth.setHost(getServerName());
		auth.setPort(getServerPort());
		auth.setPath(getRequestURI());
		auth.setQuerystring(getQueryString());
		auth.setIpAddress(getRemoteAddr());
		auth.setHeaders(getHeaderMap());
		auth.setPerformAuthorization(settings.isDelegateAuthorization());
		auth.setBypassRateLimiting(settings.isBypassRateLimiting());

		return auth;
	}

	/**
	 * Calculates the size of the request.
	 * 
	 * @return the request size.
	 */
	public int getDataSize() {
		int size = 0;
		Enumeration<String> headerNames = getHeaderNames();

		size += getMethod().length() + 1; // + " "
		size += getFullURL().length() + 10; // + " HTTP/1.X\n"

		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = getHeaders(headerName);

			while (headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				size += headerName.length() + 2; // + ": "
				size += headerValue.length() + 1; // + \n
			}
		}

		size += body.length + 1; // + \n for blank line

		return size;
	}
}
