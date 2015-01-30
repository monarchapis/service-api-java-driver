package com.monarchapis.driver.service.v1.impl;

import java.io.IOException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.authentication.AuthenticationSigner;
import com.monarchapis.driver.authentication.ProviderCredentials;
import com.monarchapis.driver.service.v1.EventsApi;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public class EventsApiDriver extends AbstractEnvironmentDriver implements EventsApi {
	private static final Logger logger = LoggerFactory.getLogger(EventsApiDriver.class);

	public EventsApiDriver(String url, ProviderCredentials providerCredentials, ServiceInfoResolver serviceInfoResolver) {
		super(url, providerCredentials, serviceInfoResolver);
	}

	public EventsApiDriver(String url, ProviderCredentials providerCredentials,
			ServiceInfoResolver serviceInfoResolver, AuthenticationSigner authenticationSigner) {
		super(url, providerCredentials, serviceInfoResolver, authenticationSigner);
	}

	@Override
	public void sendEvent(String eventType, Object data) {
		try {
			String url = "/events/v1/" + URLEncoder.encode(eventType, "UTF-8");
			executeJsonRequest(POST, url, data);
		} catch (IOException e) {
			logger.warn("An error occurred calling the event service", e);
		}
	}
}
