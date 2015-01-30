package com.monarchapis.driver.service.v1.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.monarchapis.driver.authentication.AuthenticationSigner;
import com.monarchapis.driver.authentication.ProviderCredentials;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public abstract class AbstractEnvironmentDriver extends AbstractDriver {
	private ServiceInfoResolver serviceInfoResolver;

	public AbstractEnvironmentDriver(String url, ProviderCredentials providerCredentials,
			ServiceInfoResolver serviceInfoResolver) {
		super(url, providerCredentials);
		this.serviceInfoResolver = serviceInfoResolver;
	}

	public AbstractEnvironmentDriver(String url, ProviderCredentials providerCredentials,
			ServiceInfoResolver serviceInfoResolver, AuthenticationSigner authenticationSigner) {
		super(url, providerCredentials, authenticationSigner);
		this.serviceInfoResolver = serviceInfoResolver;
	}

	@Override
	protected void setCustomHeaders(HttpRequest request, HttpHeaders headers) {
		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getUrl().getRawPath());
		headers.set("X-Environment-Id", serviceInfo.getEnvironment().getId());
	}
}
