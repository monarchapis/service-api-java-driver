package com.monarchapis.driver.jaxrs.jersey1;

import com.monarchapis.driver.model.VersionHolder;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class VersionResourceFilter implements ResourceFilter, ContainerRequestFilter {
	private String version;

	public VersionResourceFilter(String version) {
		this.version = version;
	}

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
		VersionHolder.setCurrent(version);

		return request;
	}
}
