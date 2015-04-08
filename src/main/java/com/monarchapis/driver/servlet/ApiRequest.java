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

public class ApiRequest extends HttpServletRequestWrapper {
	private static InheritableThreadLocal<ApiRequest> current = new InheritableThreadLocal<ApiRequest>();

	private byte[] body;
	private Map<String, List<String>> headers;
	private String requestId;

	public ApiRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.body = IOUtils.toByteArray(request.getInputStream());
		requestId = StringUtils.replace(UUID.randomUUID().toString(), "-", "");
	}

	public static ApiRequest getCurrent() {
		return current.get();
	}

	public static void setCurrent(ApiRequest context) {
		if (context != null) {
			current.set(context);
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

	public String getFullURL() {
		StringBuffer requestURL = getRequestURL();
		String queryString = getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

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

	public String getIpAddress() {
		String ipAddress = StringUtils.trimToNull(getHeader("X-Forwarded-For"));

		// If there is an X-Forwarded-For header, pull the first IP in the comma
		// separated list.
		return (ipAddress != null) ? StringUtils.split(ipAddress, ',')[0].trim() : getRemoteAddr();
	}

	public AuthenticationRequest createAuthorizationRequest() {
		AuthenticationRequest auth = new AuthenticationRequest();
		AuthenticationSettings settings = ServiceResolver.getInstance().getService(AuthenticationSettings.class);

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
