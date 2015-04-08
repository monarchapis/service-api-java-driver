package com.monarchapis.driver.analytics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.model.VersionHolder;
import com.monarchapis.driver.service.v1.AnalyticsApi;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

public class MonarchV1AnalyticsHandler implements AnalyticsHandler {
	private static final Logger logger = LoggerFactory.getLogger(MonarchV1AnalyticsHandler.class);

	@Inject
	private AnalyticsApi analyticsApi;

	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void collect(ApiRequest request, ApiResponse response, long ms) {
		ApiContext context = ApiContext.getCurrent();

		if (context == null || analyticsApi == null || serviceInfoResolver == null) {
			return;
		}

		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getRequestURI());
		String serviceId = serviceInfo.getService().getId();
		String providerId = serviceInfo.getProvider().getId();
		String operationName = OperationNameHolder.getCurrent();
		String version = VersionHolder.getCurrent();
		String applicationId = context.getApplication() != null ? context.getApplication().getId() : null;
		String clientId = context.getClient() != null ? context.getClient().getId() : null;
		ApiError error = ErrorHolder.getCurrent();

		if (operationName == null) {
			operationName = "unknown";
		}

		long requestSize = (long) request.getDataSize();
		long responseSize = (long) response.getDataSize();

		ObjectNode event = JsonNodeFactory.instance.objectNode();
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
		event.put("token_id", context.getToken() != null ? context.getToken().getId() : null);
		event.put("user_id", context.getPrincipal() != null ? context.getPrincipal().getId() : null);
		event.put("host", request.getServerName());
		event.put("port", request.getServerPort());
		event.put("path", request.getRequestURI());
		event.put("verb", request.getMethod());
		event.set("parameters", getParameters(request));
		event.set("headers", getHeaders(request));
		event.put("client_ip", request.getIpAddress());
		event.put("user_agent", request.getHeader("User-Agent"));

		analyticsApi.event("traffic", event);
	}

	private ObjectNode getParameters(HttpServletRequest request) {
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

	private ObjectNode getHeaders(HttpServletRequest request) {
		ObjectNode params = objectMapper.createObjectNode();
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);

			params.put(name, value);
		}

		return params;
	}

	public void setAnalyticsApi(AnalyticsApi analyticsApi) {
		this.analyticsApi = analyticsApi;
	}

	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}
}
