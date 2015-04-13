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

package com.monarchapis.driver.service.v1.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.AuthorizationDetails;
import com.monarchapis.driver.model.GrantedApplication;
import com.monarchapis.driver.model.LocaleInfo;
import com.monarchapis.driver.model.Message;
import com.monarchapis.driver.model.Token;
import com.monarchapis.driver.model.TokenRequest;
import com.monarchapis.driver.service.v1.ServiceApi;

public class ServiceApiDriver extends AbstractEnvironmentDriver implements ServiceApi {
	private static final Logger logger = LoggerFactory.getLogger(ServiceApiDriver.class);

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		long begin = System.currentTimeMillis();

		try {
			return executeJsonRequest(POST, "/service/v1/requests/authenticate", authenticationRequest,
					AuthenticationResponse.class);
		} finally {
			long duration = System.currentTimeMillis() - begin;
			logger.debug("Authentication took {}ms", duration);
		}
	}

	@Override
	public AuthorizationDetails getAuthorizationDetails(String authorizationScheme, String apiKey, String callbackUri,
			Set<String> permissions) {
		AuthorizationRequest request = new AuthorizationRequest(authorizationScheme, apiKey, callbackUri, permissions);

		return executeJsonRequest(POST, "/service/v1/authorizations/details", request, AuthorizationDetails.class);
	}

	@Override
	public boolean authenticateClient(String authorizationScheme, String apiKey, String sharedSecret) {
		AuthenticateClientRequest request = new AuthenticateClientRequest(authorizationScheme, apiKey, sharedSecret);
		HttpResponse response = executeJsonRequest(POST, "/service/v1/clients/authenticate", request);

		return response.getStatusCode() == 200;
	}

	@Override
	public Token getToken(String apiKey, String token, String callbackUri) {
		GetTokenRequest request = new GetTokenRequest(apiKey, token, callbackUri);

		return executeJsonRequest(GET, //
				"/service/v1/tokens" //
						+ "?apiKey=" //
						+ encodeUriComponent(apiKey) //
						+ "&token=" //
						+ encodeUriComponent(token) //
						+ ((callbackUri != null) ? ("&callbackUri=" + encodeUriComponent(callbackUri)) : ""), //
				request, Token.class);
	}

	@Override
	public Token getTokenByRefresh(String apiKey, String refreshToken, String callbackUri) {
		GetTokenRequest request = new GetTokenRequest(apiKey, refreshToken, callbackUri);

		return executeJsonRequest(GET, //
				"/service/v1/tokens" //
						+ "?apiKey=" //
						+ encodeUriComponent(apiKey) //
						+ "&refresh=" //
						+ encodeUriComponent(refreshToken) //
						+ ((callbackUri != null) ? ("&callbackUri=" + encodeUriComponent(callbackUri)) : ""), //
				request, Token.class);
	}

	@Override
	public boolean revokeToken(String apiKey, String token, String callbackUri) {
		GetTokenRequest request = new GetTokenRequest(apiKey, token, callbackUri);
		HttpResponse response = executeJsonRequest(DELETE, //
				"/service/v1/tokens" //
						+ "?apiKey=" //
						+ encodeUriComponent(apiKey) //
						+ "&token=" //
						+ encodeUriComponent(token) //
						+ ((callbackUri != null) ? ("&callbackUri=" + encodeUriComponent(callbackUri)) : ""), //
				request);

		return response.getStatusCode() == 200;
	}

	@Override
	public Token createToken(TokenRequest token) {
		return executeJsonRequest(//
				POST, //
				"/service/v1/tokens", //
				token, //
				Token.class);
	}

	@Override
	public Token refreshToken(Token token) {
		return executeJsonRequest(//
				POST, //
				"/service/v1/tokens/refresh", //
				token, //
				Token.class);
	}

	@Override
	public List<Message> getPermissionMessages(List<LocaleInfo> locales, Set<String> permissions) {
		GetPermissionMessagesRequest request = new GetPermissionMessagesRequest(locales, permissions);
		GetPermissionMessagesResponse response = executeJsonRequest(POST, "/service/v1/permissions/messages", request,
				GetPermissionMessagesResponse.class);

		return response.getMessages();
	}

	@Override
	public List<GrantedApplication> getGrantedApplications(List<LocaleInfo> locales, String userId) {
		GetGrantedApplicationsRequest request = new GetGrantedApplicationsRequest(locales, userId);

		return executeJsonRequest(//
				POST, //
				"/service/v1/grantedApplications", //
				request, //
				new TypeReference<List<GrantedApplication>>() {
				});
	}

	@Override
	public void revokeToken(String tokenId) {
		GenericUrl url = createUrl("/service/v1/tokens/" + encodeUriComponent(tokenId) + "/revoke");
		executeRequest(DELETE, url);
	}

	@SuppressWarnings("unused")
	private static class AuthenticateClientRequest {
		private String authorizationScheme;
		private String apiKey;
		private String sharedSecret;

		public AuthenticateClientRequest(String authorizationScheme, String apiKey, String sharedSecret) {
			this.authorizationScheme = authorizationScheme;
			this.apiKey = apiKey;
			this.sharedSecret = sharedSecret;
		}

		public String getAuthorizationScheme() {
			return authorizationScheme;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getSharedSecret() {
			return sharedSecret;
		}
	}

	@SuppressWarnings("unused")
	private static class AuthorizationRequest {
		private String authorizationScheme;
		private String apiKey;
		private String callbackUri;
		private Set<String> permissions;

		public AuthorizationRequest(String authorizationScheme, String apiKey, String callbackUri,
				Set<String> permissions) {
			this.authorizationScheme = authorizationScheme;
			this.apiKey = apiKey;
			this.callbackUri = callbackUri;
			this.permissions = permissions;
		}

		public String getAuthorizationScheme() {
			return authorizationScheme;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getCallbackUri() {
			return callbackUri;
		}

		public Set<String> getPermissions() {
			return permissions;
		}
	}

	@SuppressWarnings("unused")
	private static class GetTokenRequest {
		private String apiKey;
		private String token;
		private String callbackUri;

		public GetTokenRequest(String apiKey, String token, String callbackUri) {
			this.apiKey = apiKey;
			this.token = token;
			this.callbackUri = callbackUri;
		}

		public String getApiKey() {
			return apiKey;
		}

		public String getToken() {
			return token;
		}

		public String getCallbackUri() {
			return callbackUri;
		}
	}

	@SuppressWarnings("unused")
	private static class GetPermissionMessagesRequest {
		private List<LocaleInfo> locales;
		private Set<String> permissions;

		public GetPermissionMessagesRequest(List<LocaleInfo> locales, Set<String> permissions) {
			this.locales = locales;
			this.permissions = permissions;
		}

		public List<LocaleInfo> getLocales() {
			return locales;
		}

		public Set<String> getPermissions() {
			return permissions;
		}
	}

	@SuppressWarnings("unused")
	private static class GetGrantedApplicationsRequest {
		private List<LocaleInfo> locales;
		private String userId;

		public GetGrantedApplicationsRequest(List<LocaleInfo> locales, String userId) {
			this.locales = locales;
			this.userId = userId;
		}

		public List<LocaleInfo> getLocales() {
			return locales;
		}

		public String getUserId() {
			return userId;
		}
	}

	@SuppressWarnings("unused")
	private static class GetPermissionMessagesResponse {
		private List<Message> messages;

		public List<Message> getMessages() {
			return messages;
		}

		public void setMessages(List<Message> messages) {
			this.messages = messages;
		}
	}
}
