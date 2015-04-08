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

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.monarchapis.driver.analytics.AnalyticsHandler;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;
import com.monarchapis.driver.util.ServiceResolver;

public class ApiFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
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
			VersionHolder.setCurrent("1"); // Default

			chain.doFilter(apiRequest, apiResponse);
		} finally {
			captureResults(apiRequest, apiResponse, begin);

			ApiContext.remove();
			ApiRequest.remove();
			HttpResponseHolder.remove();
			OperationNameHolder.remove();
			VersionHolder.remove();
			ErrorHolder.remove();
			BypassAnalyticsHolder.remove();
		}
	}

	@Override
	public void destroy() {
	}

	private void captureResults(ApiRequest request, ApiResponse response, long begin) {
		try {
			long ms = System.currentTimeMillis() - begin;

			boolean bypassAnalytics = BooleanUtils.isTrue(BypassAnalyticsHolder.getCurrent());
			Optional<AnalyticsHandler> handler = ServiceResolver.getInstance().getOptional(AnalyticsHandler.class);

			if (bypassAnalytics || !handler.isPresent()) {
				return;
			}

			handler.get().collect(request, response, ms);
		} catch (Throwable e) {
			logger.error("Could not send API hit result", e);
		}
	}
}
