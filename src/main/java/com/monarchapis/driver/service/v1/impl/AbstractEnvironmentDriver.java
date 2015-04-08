package com.monarchapis.driver.service.v1.impl;

import javax.inject.Inject;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public abstract class AbstractEnvironmentDriver extends AbstractDriver {
	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	@Override
	protected void setCustomHeaders(HttpRequest request, HttpHeaders headers) {
		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getUrl().getRawPath());
		headers.set("X-Environment-Id", serviceInfo.getEnvironment().getId());
	}

	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}
}
