package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.jaxrs.common.AbstractAuthorizeFilter;

@Priority(Priorities.AUTHORIZATION)
public class AuthorizeRequestFilter extends AbstractAuthorizeFilter implements ContainerRequestFilter {
	public AuthorizeRequestFilter(String[] client, boolean user, String[] delegated, Claim[] claims,
			BigDecimal requestWeight) {
		super(client, user, delegated, claims, requestWeight);
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		checkPermissions();
	}
}