package com.monarchapis.driver.service.v1.impl;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monarchapis.driver.authentication.AuthenticationSigner;
import com.monarchapis.driver.authentication.ProviderCredentials;
import com.monarchapis.driver.service.v1.AnalyticsApi;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public class AnalyticsApiDriver extends AbstractEnvironmentDriver implements AnalyticsApi {
	private static final Logger logger = LoggerFactory.getLogger(AnalyticsApiDriver.class);

	public AnalyticsApiDriver(String url, ProviderCredentials providerCredentials,
			ServiceInfoResolver serviceInfoResolver) {
		super(url, providerCredentials, serviceInfoResolver);
	}

	public AnalyticsApiDriver(String url, ProviderCredentials providerCredentials,
			ServiceInfoResolver serviceInfoResolver, AuthenticationSigner authenticationSigner) {
		super(url, providerCredentials, serviceInfoResolver, authenticationSigner);
	}

	@Override
	public void event(String eventType, ObjectNode data) {
		long begin = System.currentTimeMillis();

		try {
			executeJsonRequest(POST, "/analytics/v1/" + URLEncoder.encode(eventType, "UTF-8") + "/events", data);
		} catch (Exception e) {
			throw new RuntimeException("Could not send event", e);
		} finally {
			long duration = System.currentTimeMillis() - begin;
			logger.debug("Send result took {}ms", duration);
		}
	}
}
