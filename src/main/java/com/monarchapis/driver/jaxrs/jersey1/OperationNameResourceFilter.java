package com.monarchapis.driver.jaxrs.jersey1;

import com.monarchapis.driver.model.OperationNameHolder;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class OperationNameResourceFilter implements ResourceFilter, ContainerRequestFilter {
	private String operationName;

	public OperationNameResourceFilter(String operationName) {
		this.operationName = operationName;
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
		OperationNameHolder.setCurrent(operationName);

		return request;
	}
}
