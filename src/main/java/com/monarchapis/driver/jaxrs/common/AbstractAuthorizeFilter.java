package com.monarchapis.driver.jaxrs.common;

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.util.ServiceResolver;

public abstract class AbstractAuthorizeFilter {
	private String[] client;
	private boolean user;
	private String[] delegated;
	private Claim[] claims;
	private BigDecimal requestWeight;

	public AbstractAuthorizeFilter(String[] client, boolean user, String[] delegated, Claim[] claims,
			BigDecimal requestWeight) {
		this.client = client;
		this.user = user;
		this.delegated = delegated;
		this.claims = claims;
		this.requestWeight = requestWeight;
	}

	protected void checkPermissions() {
		ServiceResolver serviceResolver = ServiceResolver.getInstance();
		Authenticator authenticator = serviceResolver.getService(Authenticator.class);

		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
	}

	public String[] getClient() {
		return client;
	}

	public boolean isUser() {
		return user;
	}

	public String[] getDelegated() {
		return delegated;
	}

	public Claim[] getClaims() {
		return claims;
	}

	public BigDecimal getRequestWeight() {
		return requestWeight;
	}
}
