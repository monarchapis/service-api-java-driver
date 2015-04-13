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

package com.monarchapis.driver.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an authentication request for the API request.
 * 
 * @author Phil Kedy
 */
public class AuthenticationRequest {
	/**
	 * The HTTP method.
	 */
	private String method;

	/**
	 * The protocol (e.g. http or https).
	 */
	private String protocol;

	/**
	 * The API server host.
	 */
	private String host;

	/**
	 * The API server port.
	 */
	private int port;

	/**
	 * The request path.
	 */
	private String path;

	/**
	 * The request query string.
	 */
	private String querystring;

	/**
	 * The request headers.
	 */
	private Map<String, List<String>> headers;

	/**
	 * The request remote IP address.
	 */
	private String ipAddress;

	/**
	 * The payload hashes calculated by the service.
	 */
	private Map<String, Map<String, String>> payloadHashes;

	/**
	 * The request weight used for rate limit counting.
	 */
	private BigDecimal requestWeight;

	/**
	 * Flag that instructs Monarch to perform authorization based on the
	 * configured operations of the service.
	 */
	private boolean performAuthorization;

	/**
	 * Flag that instructs Monarch to bypass rate limiting. This should be
	 * enabled when using the API Gateway.
	 */
	private boolean bypassRateLimiting;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String url) {
		this.path = url;
	}

	public String getQuerystring() {
		return querystring;
	}

	public void setQuerystring(String querystring) {
		this.querystring = querystring;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Map<String, Map<String, String>> getPayloadHashes() {
		return payloadHashes;
	}

	public void setPayloadHashes(Map<String, Map<String, String>> payloadHashes) {
		this.payloadHashes = payloadHashes;
	}

	public String getPayloadHash(String scheme, String algorithm) {
		String hash = null;

		if (payloadHashes != null) {
			Map<String, String> schemeMap = payloadHashes.get(scheme);

			if (schemeMap != null) {
				hash = schemeMap.get(algorithm);
			}
		}

		return hash;
	}

	public void setPayloadHash(String scheme, String algorithm, String hash) {
		if (payloadHashes == null) {
			payloadHashes = new HashMap<String, Map<String, String>>();
		}

		Map<String, String> schemeMap = payloadHashes.get(scheme);

		if (schemeMap == null) {
			schemeMap = new HashMap<String, String>();
			payloadHashes.put(scheme, schemeMap);
		}

		schemeMap.put(algorithm, hash);
	}

	public BigDecimal getRequestWeight() {
		return requestWeight;
	}

	public void setRequestWeight(BigDecimal requestWeight) {
		this.requestWeight = requestWeight;
	}

	public boolean isPerformAuthorization() {
		return performAuthorization;
	}

	public void setPerformAuthorization(boolean performAuthorization) {
		this.performAuthorization = performAuthorization;
	}

	public boolean isBypassRateLimiting() {
		return bypassRateLimiting;
	}

	public void setBypassRateLimiting(boolean bypassRateLimiting) {
		this.bypassRateLimiting = bypassRateLimiting;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
