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

package com.monarchapis.driver.service.v1.mock;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.AuthorizationDetails;
import com.monarchapis.driver.model.GrantedApplication;
import com.monarchapis.driver.model.LocaleInfo;
import com.monarchapis.driver.model.Message;
import com.monarchapis.driver.model.Token;
import com.monarchapis.driver.model.TokenRequest;
import com.monarchapis.driver.service.v1.ServiceApi;

public class ServiceApiDriver implements ServiceApi {
	private static final Logger logger = LoggerFactory.getLogger(ServiceApiDriver.class);

	private ApiContext apiContext;

	public ServiceApiDriver() {
	}

	public ServiceApiDriver(ApiContext apiContext) {
		setApiContext(apiContext);
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		AuthenticationResponse response = new AuthenticationResponse();

		response.setCode(200);
		response.setContext(apiContext);

		return response;
	}

	@Override
	public AuthorizationDetails getAuthorizationDetails(String authorizationScheme, String apiKey, String callbackUri,
			Set<String> permissions) {
		throw new UnsupportedOperationException("getAuthorizationDetails not yet implemented");
	}

	@Override
	public boolean authenticateClient(String authorizationScheme, String apiKey, String sharedSecret) {
		return true;
	}

	@Override
	public Token getToken(String apiKey, String token, String callbackUri) {
		throw new UnsupportedOperationException("getToken not yet implemented");
	}

	@Override
	public Token getTokenByRefresh(String apiKey, String refreshToken, String callbackUri) {
		throw new UnsupportedOperationException("getTokenByRefresh not yet implemented");
	}

	@Override
	public boolean revokeToken(String apiKey, String token, String callbackUri) {
		return true;
	}

	@Override
	public Token createToken(TokenRequest token) {
		throw new UnsupportedOperationException("createToken not yet implemented");
	}

	@Override
	public Token refreshToken(Token token) {
		throw new UnsupportedOperationException("refreshToken not yet implemented");
	}

	@Override
	public List<Message> getPermissionMessages(List<LocaleInfo> locales, Set<String> permissions) {
		throw new UnsupportedOperationException("getPermissionMessages not yet implemented");
	}

	@Override
	public List<GrantedApplication> getGrantedApplications(List<LocaleInfo> locales, String userId) {
		throw new UnsupportedOperationException("getGrantedApplications not yet implemented");
	}

	@Override
	public void revokeToken(String tokenId) {
	}

	public ApiContext getApiContext() {
		return apiContext;
	}

	public void setApiContext(ApiContext apiContext) {
		this.apiContext = apiContext;
		logger.info("API context set to: {}", apiContext);
	}
}
