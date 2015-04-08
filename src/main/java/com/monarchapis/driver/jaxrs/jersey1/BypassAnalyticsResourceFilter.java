package com.monarchapis.driver.jaxrs.jersey1;

import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class BypassAnalyticsResourceFilter implements ResourceFilter, ContainerRequestFilter {
	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		BypassAnalyticsHolder.setCurrent(true);

		return request;
	}
}
