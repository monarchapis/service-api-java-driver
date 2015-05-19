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
import java.util.List;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.monarchapis.driver.analytics.AnalyticsHandler;
import com.monarchapis.driver.authentication.ClaimsProcessor;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.Claims;
import com.monarchapis.driver.model.ClaimsHolder;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;
import com.monarchapis.driver.util.ServiceResolver;

/**
 * A servlet filter that handles wrapping the request and response, continuing
 * the request down the filter chain, and sending the traffic statistics to the
 * {@link AnalyticsHandler}.
 * 
 * @author Phil Kedy
 */
public class ApiFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
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

			readClaimsFromRequest(apiRequest);

			chain.doFilter(apiRequest, apiResponse);
		} finally {
			captureResults(apiRequest, apiResponse, begin);

			ClaimsHolder.remove();
			ApiRequest.remove();
			HttpResponseHolder.remove();
			OperationNameHolder.remove();
			VersionHolder.remove();
			ErrorHolder.remove();
			BypassAnalyticsHolder.remove();
		}
	}

	private void readClaimsFromRequest(ApiRequest apiRequest) {
		List<ClaimsProcessor> processors = ServiceResolver.getInstance().getInstancesOf(ClaimsProcessor.class);

		for (ClaimsProcessor processor : processors) {
			ObjectNode claims = processor.getClaims(apiRequest);

			if (claims != null) {
				ClaimsHolder.setCurrent(new Claims(claims));

				return;
			}
		}
	}

	/**
	 * If an {@link AnalyticsHandler} is defined, it will invoke the collect
	 * method in order to send traffic statistics to the analytics engine.
	 * 
	 * @param request
	 *            The API request
	 * @param response
	 *            The API response
	 * @param begin
	 *            The time in milliseconds when the request began.
	 */
	private void captureResults(ApiRequest request, ApiResponse response, long begin) {
		try {
			long ms = System.currentTimeMillis() - begin;

			boolean bypassAnalytics = BooleanUtils.isTrue(BypassAnalyticsHolder.getCurrent());
			Optional<AnalyticsHandler> handler = ServiceResolver.getInstance().optional(AnalyticsHandler.class);

			if (bypassAnalytics || !handler.isPresent()) {
				return;
			}

			handler.get().collect(request, response, ms);
		} catch (Throwable e) {
			logger.error("Could not send API hit result", e);
		}
	}
}
