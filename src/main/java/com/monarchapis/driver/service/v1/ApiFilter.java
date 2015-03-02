package com.monarchapis.driver.service.v1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monarchapis.driver.authentication.AuthenticationSigner;
import com.monarchapis.driver.authentication.ProviderCredentials;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.hash.RequestHasher;
import com.monarchapis.driver.hash.RequestHasherRegistry;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.HasherAlgorithm;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.impl.AnalyticsApiDriver;
import com.monarchapis.driver.service.v1.impl.EventsApiDriver;
import com.monarchapis.driver.service.v1.impl.OpenApiDriver;
import com.monarchapis.driver.service.v1.impl.ServiceApiDriver;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

public class ApiFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServiceContainer container = ServiceContainer.getInstance();

		setRequestHashersFromConfig(filterConfig, container);
		setHasherAlgorithmsFromConfig(filterConfig, container);
		setDriverApisFromConfig(filterConfig, container);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		long begin = System.currentTimeMillis();
		ApiRequest apiRequest = new ApiRequest(httpRequest);
		ApiResponse apiResponse = new ApiResponse(httpResponse);

		try {
			HttpResponseHolder.setCurrent(apiResponse);
			ApiRequest.setCurrent(apiRequest);

			chain.doFilter(apiRequest, apiResponse);
		} finally {
			try {
				captureResults(apiRequest, apiResponse, begin);
			} catch (Throwable e) {
				// Needs to be ignored for cleanup below
			}

			ApiContext.remove();
			ApiRequest.remove();
			HttpResponseHolder.remove();
			OperationNameHolder.remove();
			ErrorHolder.remove();
		}
	}

	@Override
	public void destroy() {
		ServiceContainer.destroy();
	}

	private void captureResults(ApiRequest request, ApiResponse response, long begin) {
		try {
			long ms = System.currentTimeMillis() - begin;
			ApiContext context = ApiContext.getCurrent();
			ServiceContainer container = ServiceContainer.getInstance();
			AnalyticsApi analyticsApi = container.getAnalyticsApi();
			ServiceInfoResolver serviceInfoResolver = container.getServiceInfoResolver();

			if (container.isCaptureAnalytics() && context != null && analyticsApi != null
					&& serviceInfoResolver != null) {
				ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getRequestURI());
				String serviceId = serviceInfo.getService().getId();
				String providerId = serviceInfo.getProvider().getId();
				String operationName = OperationNameHolder.getCurrent();
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
				event.put("service_version", "1"); // TODO
				event.put("operation_name", operationName);
				event.put("provider_id", providerId);
				event.put("request_size", requestSize);
				event.put("response_size", responseSize);
				event.put("response_time", ms);
				event.put("status_code", error == null ? 200 : error.getCode());
				event.put("error_reason", error == null ? "ok" : error.getReason());
				event.put("cache_hit", false);
				event.put("token_id", context.getToken() != null ? context.getToken().getId() : null);
				event.put("user_id", context.getPrincipal() != null ? context.getPrincipal().getId() : null);
				event.put("host", request.getServerName());
				event.put("path", request.getRequestURI());
				event.put("port", request.getServerPort());
				event.put("verb", request.getMethod());
				event.set("parameters", getParameters(request));
				event.set("headers", getHeaders(request));
				event.put("client_ip", request.getIpAddress());
				event.put("user_agent", request.getHeader("User-Agent"));

				analyticsApi.event("traffic", event);
			}
		} catch (Throwable e) {
			logger.error("Could not send API hit result", e);
		}
	}

	private static void setRequestHashersFromConfig(FilterConfig filterConfig, ServiceContainer container)
			throws ServletException {
		String requestHasherClassNames = StringUtils.trimToEmpty(filterConfig
				.getInitParameter("requestHasherClassNames"));
		List<RequestHasher> hashers = requestHashersFromString(requestHasherClassNames);

		if (hashers.size() > 0) {
			container.setRequestHasherRegistry(new RequestHasherRegistry(hashers));
		}
	}

	private static void setHasherAlgorithmsFromConfig(FilterConfig filterConfig, ServiceContainer container) {
		String hasherAlgorithmsAsString = StringUtils.trimToEmpty(filterConfig.getInitParameter("hasherAlgorithms"));
		List<HasherAlgorithm> hasherAlgorithms = hasherAlgorithmsFromString(hasherAlgorithmsAsString);

		if (hasherAlgorithms.size() > 0) {
			container.setHasherAlgorithms(hasherAlgorithms);
		}
	}

	private static void setDriverApisFromConfig(FilterConfig filterConfig, ServiceContainer container)
			throws ServletException {
		String environmentName = filterConfig.getInitParameter("environmentName");
		String serviceName = filterConfig.getInitParameter("serviceName");
		String serviceApiUrl = filterConfig.getInitParameter("serviceApiUrl");
		String analyticsApiUrl = filterConfig.getInitParameter("analyticsApiUrl");
		String eventsApiUrl = filterConfig.getInitParameter("eventsApiUrl");
		String providerKey = filterConfig.getInitParameter("providerKey");
		String sharedSecret = filterConfig.getInitParameter("sharedSecret");
		String authenticator = filterConfig.getInitParameter("authenticationSigner");

		AuthenticationSigner signer = null;

		if (authenticator != null) {
			try {
				signer = (AuthenticationSigner) Class.forName(authenticator).newInstance();
			} catch (Exception e) {
				throw new ServletException("Could not instanticate " + authenticator, e);
			}
		}

		if (environmentName != null && serviceName != null && providerKey != null) {
			if (serviceApiUrl != null) {
				ProviderCredentials providerCreds = new ProviderCredentials(providerKey, sharedSecret);

				OpenApi openApi = new OpenApiDriver(serviceApiUrl);

				ServiceInfoResolver serviceInfoResolver = new SingleServiceInfoResolver(openApi, environmentName,
						serviceName, providerKey);

				container.setServiceInfoResolver(serviceInfoResolver);

				ServiceApi serviceApi = new ServiceApiDriver(serviceApiUrl, providerCreds, serviceInfoResolver, signer);

				container.setServiceApi(serviceApi);

				if (analyticsApiUrl != null) {
					AnalyticsApi analyticsApi = new AnalyticsApiDriver(analyticsApiUrl, providerCreds,
							serviceInfoResolver, signer);

					container.setAnalyticsApi(analyticsApi);
				}

				if (eventsApiUrl != null) {
					EventsApi eventsApi = new EventsApiDriver(eventsApiUrl, providerCreds, serviceInfoResolver, signer);

					container.setEventsApi(eventsApi);
				}
			}
		}
	}

	private static List<RequestHasher> requestHashersFromString(String requestHasherClassNames) throws ServletException {
		String[] classNamesArray = StringUtils.split(requestHasherClassNames, ",;");
		List<RequestHasher> hashers = new ArrayList<RequestHasher>();

		for (String className : classNamesArray) {
			className = className.trim();

			try {
				Class<?> hasherClass = Class.forName(className);
				RequestHasher hasher = (RequestHasher) hasherClass.newInstance();
				hashers.add(hasher);
			} catch (Exception e) {
				throw new ServletException("Could not instantiate request hasher class " + className, e);
			}
		}

		return hashers;
	}

	private static List<HasherAlgorithm> hasherAlgorithmsFromString(String hasherAlgorithms) {
		hasherAlgorithms = StringUtils.trimToEmpty(hasherAlgorithms);
		String[] hasherArray = StringUtils.split(hasherAlgorithms, ';');
		List<HasherAlgorithm> list = new ArrayList<HasherAlgorithm>(hasherArray.length);

		for (String hasher : hasherArray) {
			String[] parts = StringUtils.split(hasher, '=');

			if (parts.length == 2) {
				String name = parts[0].trim();
				String[] algos = StringUtils.split(parts[1].trim(), ',');

				if (algos.length > 0) {
					for (int i = 0; i < algos.length; i++) {
						algos[i] = algos[i].trim();
					}

					list.add(new HasherAlgorithm(name, algos));
				}
			}
		}

		return list;
	}

	public static ObjectNode getParameters(HttpServletRequest request) throws UnsupportedEncodingException {
		ObjectNode params = null;
		String qs = request.getQueryString();

		if (qs != null) {
			params = JsonNodeFactory.instance.objectNode();

			for (String param : qs.split("&")) {
				String pair[] = param.split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = (pair.length > 1) ? URLDecoder.decode(pair[1], "UTF-8") : "";
				params.put(key, value);
			}
		}

		return params;
	}

	public static ObjectNode getHeaders(HttpServletRequest request) throws UnsupportedEncodingException {
		ObjectNode params = JsonNodeFactory.instance.objectNode();
		Enumeration<String> headerNames = request.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = request.getHeader(name);

			params.put(name, value);
		}

		return params;
	}
}
