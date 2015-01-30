package com.monarchapis.driver.jaxrs.jersey1;

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.jaxrs.common.AbstractAuthorizeFilter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthorizeResourceFilter extends AbstractAuthorizeFilter implements ResourceFilter, ContainerRequestFilter {
	public AuthorizeResourceFilter(String[] client, boolean user, String[] delegated, Claim[] claims,
			BigDecimal requestWeight) {
		super(client, user, delegated, claims, requestWeight);
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
		checkPermissions();

		return request;
	}
}
