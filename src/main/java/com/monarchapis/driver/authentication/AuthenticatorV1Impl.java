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

package com.monarchapis.driver.authentication;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.hash.RequestHasher;
import com.monarchapis.driver.hash.RequestHasherRegistry;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.HasherAlgorithm;
import com.monarchapis.driver.model.HttpHeader;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.servlet.ApiRequest;

/**
 * The main implementation for authenticating and authorizing API requests.
 * 
 * @author Phil Kedy
 */
@Named
public class AuthenticatorV1Impl implements Authenticator {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticatorV1Impl.class);

	/**
	 * The Service API implementation
	 */
	@Inject
	private ServiceApi serviceApi;

	/**
	 * The list of hasher algorithms to use on the request
	 */
	@Inject
	private List<HasherAlgorithm> hasherAlgorithms;

	/**
	 * The request hasher registry used to lookup a request hasher
	 */
	@Inject
	private RequestHasherRegistry requestHasherRegistry;

	/**
	 * The API error factory for throwing exception when authentication or
	 * authorization fails.
	 */
	@Inject
	private ApiErrorFactory apiErrorFactory;

	/**
	 * @param requestWeight
	 *            The request weight to count for rate limiting
	 * @param client
	 *            The required client permissions. A single match passes this
	 *            check.
	 * @param delegated
	 *            The required permissions delegated to the user. A single match
	 *            passes this check.
	 * @param user
	 *            Flag that denotes that an authenticated user is required.
	 * @param claims
	 *            The claims required by the user. A single match passes this
	 *            check.
	 */
	@Override
	public void performAccessChecks(BigDecimal requestWeight, String[] client, String[] delegated, boolean user,
			Claim[] claims) {
		ApiContext apiContext = getApiContext(requestWeight);

		if (apiContext == null) {
			throw apiErrorFactory.exception("forbidden");
		}

		for (String permission : client) {
			if (!apiContext.hasClientPermission(permission)) {
				throw apiErrorFactory.exception("forbidden");
			}
		}

		// Only check delegated permissions if a token is part of the API
		// context.
		if (delegated.length > 0 && apiContext.getToken() != null) {
			for (String permission : delegated) {
				if (!apiContext.hasDelegatedPermission(permission)) {
					throw apiErrorFactory.exception("forbidden");
				}
			}
		}

		if (user || claims.length > 0) {
			if (apiContext.getPrincipal() == null) {
				throw apiErrorFactory.exception("invalidAccessToken");
			}

			for (Claim claim : claims) {
				if (!apiContext.hasUserClaim(claim.type(), claim.value())) {
					throw apiErrorFactory.exception("forbidden");
				}
			}
		}
	}

	/**
	 * Returns the API Context for the current request. If the context is not
	 * established, it will invoke the Service API to obtain it.
	 * 
	 * @param requestWeight
	 *            The request weight to count in rate limiting
	 * @return the API context object.
	 */
	private ApiContext getApiContext(BigDecimal requestWeight) {
		ApiContext apiContext = ApiContext.getCurrent();

		if (apiContext.getClient() == null) {
			AuthenticationResponse authResponse = null;

			try {
				authResponse = processAuthentication(requestWeight);
			} catch (Exception e) {
				throwSystemException(e);
			}

			if (authResponse == null) {
				throwSystemException(null);
			}

			ApiContext responseContext = authResponse.getContext();

			if (responseContext != null) {
				apiContext.copy(responseContext);
			}

			if (authResponse.getCode() != 200) {
				// Map the exception based on the error reason
				throw apiErrorFactory.exception(authResponse.getReason());
			}
		}

		return apiContext;
	}

	/**
	 * Prepares an authentication request and sends it to the Service API.
	 * Response headers are added the API response.
	 * 
	 * @param requestWeight
	 *            The request weight to count in rate limiting
	 * @return the authentication response.
	 */
	private AuthenticationResponse processAuthentication(BigDecimal requestWeight) {
		ApiRequest apiRequest = ApiRequest.getCurrent();
		HttpServletResponse httpResponse = HttpResponseHolder.getCurrent();

		AuthenticationRequest authRequest = prepareAuthenticationRequest(apiRequest);
		authRequest.setRequestWeight(requestWeight);
		AuthenticationResponse authResponse = serviceApi.authenticate(authRequest);

		if (authResponse.getResponseHeaders() != null) {
			for (HttpHeader header : authResponse.getResponseHeaders()) {
				httpResponse.addHeader(header.getName(), header.getValue());
			}
		}

		return authResponse;
	}

	/**
	 * Converts the current API request into an authentication request and
	 * calculates request hashes.
	 * 
	 * @param request
	 *            The API request
	 * @return the authentication request to send to the Service API.
	 */
	private AuthenticationRequest prepareAuthenticationRequest(ApiRequest request) {
		AuthenticationRequest authRequest = request.createAuthorizationRequest();

		if (hasherAlgorithms != null) {
			for (HasherAlgorithm hasherAlgo : hasherAlgorithms) {
				RequestHasher hasher = requestHasherRegistry.getRequestHasher(hasherAlgo.getName());

				for (String algorithm : hasherAlgo.getAlgorithms()) {
					String hash = hasher.getRequestHash(request, algorithm);

					if (hash != null) {
						authRequest.setPayloadHash(hasherAlgo.getName(), algorithm, hash);
					}
				}
			}
		}

		return authRequest;
	}

	/**
	 * Logs and throws a "service unavailable" exception.
	 * 
	 * @param exception
	 *            the exception to include in the log
	 */
	private void throwSystemException(Exception exception) {
		logger.error("Could not authenticate API request", exception);

		throw apiErrorFactory.exception("serviceUnavailable");
	}

	public void setServiceApi(ServiceApi serviceApi) {
		this.serviceApi = serviceApi;
	}

	public void setHasherAlgorithms(List<HasherAlgorithm> hasherAlgorithms) {
		this.hasherAlgorithms = hasherAlgorithms;
	}

	public void setRequestHasherRegistry(RequestHasherRegistry requestHasherRegistry) {
		this.requestHasherRegistry = requestHasherRegistry;
	}

	public void setApiErrorFactory(ApiErrorFactory apiErrorFactory) {
		this.apiErrorFactory = apiErrorFactory;
	}
}
