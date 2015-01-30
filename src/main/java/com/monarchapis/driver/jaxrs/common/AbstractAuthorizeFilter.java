package com.monarchapis.driver.jaxrs.common;

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.exception.ForbiddenException;
import com.monarchapis.driver.exception.InvalidAccessTokenException;
import com.monarchapis.driver.model.ApiContext;

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
		ApiContext apiContext = JaxRsUtils.getApiContext(requestWeight);

		if (apiContext == null) {
			throw new ForbiddenException();
		}

		for (String permission : client) {
			if (!apiContext.hasClientPermission(permission)) {
				throw new ForbiddenException();
			}
		}

		// Only check delegated permissions if a token is part of the API
		// context.
		if (delegated.length > 0 && apiContext.getToken() != null) {
			for (String permission : delegated) {
				if (!apiContext.hasDelegatedPermission(permission)) {
					throw new ForbiddenException();
				}
			}
		}

		if (user || claims.length > 0) {
			if (apiContext.getPrincipal() == null) {
				throw new InvalidAccessTokenException();
			}

			for (Claim claim : claims) {
				if (!apiContext.hasUserClaim(claim.type(), claim.value())) {
					throw new ForbiddenException();
				}
			}
		}
	}
}
