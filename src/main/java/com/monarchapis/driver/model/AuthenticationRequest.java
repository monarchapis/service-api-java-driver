package com.monarchapis.driver.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthenticationRequest {
	private String method;
	private String protocol;
	private String host;
	private int port;
	private String path;
	private String querystring;
	private Map<String, List<String>> headers;
	private String ipAddress;
	private Map<String, Map<String, String>> payloadHashes;
	private BigDecimal requestWeight;
	private boolean performAuthorization;
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
