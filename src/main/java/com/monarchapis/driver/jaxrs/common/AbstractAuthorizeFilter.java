/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monarchapis.driver.jaxrs.common;

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.util.ServiceResolver;

/**
 * Provides support to Jersey 1 & 2 classes for collecting authorization
 * parameters and performing the access checks via the
 * <code>Authenticator</code>.
 * 
 * @author Phil Kedy
 */
public abstract class AbstractAuthorizeFilter {
	/**
	 * The list of any possible client permission matches required in order for
	 * the operation to be called.
	 */
	private String[] client;

	/**
	 * Flag that denotes that an authenticated user is required.
	 */
	private boolean user;

	/**
	 * The list of any possible permission matches delegated by the user to the
	 * client in order for the operation to be called.
	 */
	private String[] delegated;

	/**
	 * The list of any possible user claim matches required in order for the
	 * operation to be called.
	 */
	private Claim[] claims;

	/**
	 * The request weight used for rate limit counting.
	 */
	private BigDecimal requestWeight;

	public AbstractAuthorizeFilter(String[] client, boolean user, String[] delegated, Claim[] claims,
			BigDecimal requestWeight) {
		this.client = client;
		this.user = user;
		this.delegated = delegated;
		this.claims = claims;
		this.requestWeight = requestWeight;
	}

	/**
	 * Performs the access checks via the <code>Authenticator</code>
	 */
	protected void checkPermissions() {
		ServiceResolver serviceResolver = ServiceResolver.getInstance();
		Authenticator authenticator = serviceResolver.required(Authenticator.class);

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
