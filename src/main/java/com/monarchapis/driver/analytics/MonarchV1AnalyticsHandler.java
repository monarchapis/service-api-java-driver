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

package com.monarchapis.driver.analytics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

import jersey.repackaged.com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.monarchapis.api.v1.client.AnalyticsApi;
import com.monarchapis.api.v1.client.EventsResource;
import com.monarchapis.api.v1.model.ObjectData;
import com.monarchapis.api.v1.model.Reference;
import com.monarchapis.api.v1.model.ServiceInfo;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.model.ApplicationContext;
import com.monarchapis.driver.model.ClaimNames;
import com.monarchapis.driver.model.Claims;
import com.monarchapis.driver.model.ClaimsHolder;
import com.monarchapis.driver.model.ClientContext;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.PrincipalContext;
import com.monarchapis.driver.model.TokenContext;
import com.monarchapis.driver.model.VersionHolder;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

/**
 * Handles collecting analytics events into a data store.
 * 
 * @author Phil Kedy
 */
public class MonarchV1AnalyticsHandler implements AnalyticsHandler, ClaimNames {
	private static final Logger logger = LoggerFactory.getLogger(MonarchV1AnalyticsHandler.class);

	@Inject
	private AnalyticsApi analyticsApi;

	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	private Set<Pattern> ignoreUriPatterns;

	public MonarchV1AnalyticsHandler() {
	}

	public MonarchV1AnalyticsHandler(AnalyticsApi analyticsApi, ServiceInfoResolver serviceInfoResolver) {
		this.analyticsApi = analyticsApi;
		this.serviceInfoResolver = serviceInfoResolver;
	}

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void collect(ApiRequest request, ApiResponse response, long ms) {
		Claims claims = ClaimsHolder.getCurrent();

		if (ignoreUriPatterns != null) {
			String uri = request.getRequestURI();
			String context = request.getContextPath();

			if (context != null && context.length() > 0) {
				uri = uri.substring(context.length());
			}

			for (Pattern pattern : ignoreUriPatterns) {
				if (pattern.matcher(uri).matches()) {
					return;
				}
			}
		}

		Optional<ApplicationContext> application = getClaimObject(claims, ApplicationContext.class, APPLICATION);
		Optional<ClientContext> client = getClaimObject(claims, ClientContext.class, CLIENT);
		Optional<TokenContext> token = getClaimObject(claims, TokenContext.class, TOKEN);
		Optional<PrincipalContext> principal = getClaimObject(claims, PrincipalContext.class, PRINCIPAL);

		if (analyticsApi == null || serviceInfoResolver == null) {
			return;
		}

		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getRequestURI());
		String serviceId = getReferenceId(serviceInfo.getService());
		String providerId = getReferenceId(serviceInfo.getProvider());
		String operationName = OperationNameHolder.getCurrent();
		String version = VersionHolder.getCurrent();
		String applicationId = application.isPresent() ? application.get().getId() : null;
		String clientId = client.isPresent() ? client.get().getId() : null;
		ApiError error = ErrorHolder.getCurrent();

		if (operationName == null) {
			operationName = "unknown";
		}

		long requestSize = (long) request.getDataSize();
		long responseSize = (long) response.getDataSize();

		ObjectData event = new ObjectData();
		event.put("request_id", request.getRequestId());
		event.put("application_id", applicationId);
		event.put("client_id", clientId);
		event.put("service_id", serviceId);
		event.put("service_version", version);
		event.put("operation_name", operationName);
		event.put("provider_id", providerId);
		event.put("request_size", requestSize);
		event.put("response_size", responseSize);
		event.put("response_time", ms);
		event.put("status_code", error == null ? response.getStatus() : error.getStatus());
		event.put("error_reason", error == null ? "ok" : error.getReason());
		event.put("cache_hit", false);
		event.put("token_id", token.isPresent() ? token.get().getId() : null);
		event.put("user_id", principal.isPresent() ? principal.get().getId() : null);
		event.put("host", request.getServerName());
		event.put("port", request.getServerPort());
		event.put("path", request.getRequestURI());
		event.put("verb", request.getMethod());
		event.put("parameters", getParameters(request));
		event.put("headers", getHeaders(request));
		event.put("client_ip", request.getIpAddress());
		event.put("user_agent", request.getHeader("User-Agent"));

		EventsResource events = analyticsApi.getEventsResource();
		events.collectEvent("traffic", event);
	}

	private <T> Optional<T> getClaimObject(Claims claims, Class<T> clazz, String claimName) {
		if (claims == null) {
			return Optional.absent();
		}

		return claims.getAs(clazz, claimName);
	}

	private String getReferenceId(Optional<Reference> reference) {
		return reference.isPresent() ? reference.get().getId() : null;
	}

	/**
	 * Converts the request query string into an object node.
	 * 
	 * @param request
	 *            The API request
	 * @return the object node if a query string exists, null otherwise.
	 */
	private ObjectNode getParameters(ApiRequest request) {
		ObjectNode params = null;
		String qs = request.getQueryString();

		if (qs != null) {
			params = objectMapper.createObjectNode();

			try {
				for (String param : qs.split("&")) {
					String pair[] = param.split("=");
					String key = URLDecoder.decode(pair[0], "UTF-8");
					String value = (pair.length > 1) ? URLDecoder.decode(pair[1], "UTF-8") : "";
					params.put(key, value);
				}
			} catch (UnsupportedEncodingException uee) {
				logger.error("Could not decode query string parameter", uee);
			}
		}

		return params;
	}

	/**
	 * Converts the request headers into an object node.
	 * 
	 * @param request
	 *            The API request
	 * @return the object node with all of the headers as key-value pairs.
	 */
	private ObjectNode getHeaders(ApiRequest request) {
		ObjectNode params = objectMapper.createObjectNode();
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);

			params.put(name, value);
		}

		return params;
	}

	/**
	 * Sets the Analytics API
	 * 
	 * @param analyticsApi
	 *            The Analytics API implementation
	 */
	public void setAnalyticsApi(AnalyticsApi analyticsApi) {
		this.analyticsApi = analyticsApi;
	}

	/**
	 * Sets the service info resolver
	 * 
	 * @param serviceInfoResolver
	 *            The service info resolver
	 */
	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}

	public void setIgnoreUriPatterns(Set<String> patterns) {
		if (patterns != null && patterns.size() > 0) {
			Iterable<Pattern> compiled = Iterables.transform(patterns, new Function<String, Pattern>() {
				@Override
				public Pattern apply(String pattern) {
					return Pattern.compile(pattern);
				}
			});

			ignoreUriPatterns = Sets.newHashSet(compiled);
		} else {
			ignoreUriPatterns = null;
		}
	}
}
