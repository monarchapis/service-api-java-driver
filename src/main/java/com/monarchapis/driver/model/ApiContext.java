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

package com.monarchapis.driver.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApiContext {
	/**
	 * Flag that denotes if an empty ApiContext should be automatically created.
	 */
	private static boolean autoCreate = true;

	/**
	 * The current ApiContext.
	 */
	private static InheritableThreadLocal<ApiContext> current = new InheritableThreadLocal<ApiContext>();

	/**
	 * The request identifier.
	 */
	private String requestId;

	/**
	 * The application context.
	 */
	private ApplicationContext application;

	/**
	 * The client context.
	 */
	private ClientContext client;

	/**
	 * The token context.
	 */
	private TokenContext token;

	/**
	 * The principal context.
	 */
	private PrincipalContext principal;

	/**
	 * The provider context.
	 */
	private ProviderContext provider;

	/**
	 * Copies the information from a request into this request.
	 * 
	 * @param from
	 *            The ApiContext to copy from.
	 */
	public void copy(ApiContext from) {
		this.requestId = from.requestId;
		this.application = from.application;
		this.client = from.client;
		this.token = from.token;
		this.principal = from.principal;
		this.provider = from.provider;
	}

	public static void setAutoCreate(boolean autoCreate) {
		ApiContext.autoCreate = autoCreate;
	}

	/**
	 * Returns the current ApiContext. If autoCreate is enabled and the
	 * ApiContext is not set, it will be created automatically.
	 * 
	 * @return The ApiContext if found or automatically created, null otherwise.
	 */
	public static ApiContext getCurrent() {
		ApiContext ret = current.get();

		if (ret == null && autoCreate) {
			ret = new ApiContext();
			current.set(ret);
		}

		return ret;
	}

	public static void setCurrent(ApiContext context) {
		if (context != null) {
			current.set(context);
		} else {
			current.remove();
		}
	}

	public static void remove() {
		current.remove();
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ApplicationContext getApplication() {
		return application;
	}

	public void setApplication(ApplicationContext application) {
		this.application = application;
	}

	public ClientContext getClient() {
		return client;
	}

	public void setClient(ClientContext client) {
		this.client = client;
	}

	public TokenContext getToken() {
		return token;
	}

	public void setToken(TokenContext token) {
		this.token = token;
	}

	public PrincipalContext getPrincipal() {
		return principal;
	}

	public void setPrincipal(PrincipalContext principal) {
		this.principal = principal;
	}

	public ProviderContext getProvider() {
		return provider;
	}

	public void setProvider(ProviderContext provider) {
		this.provider = provider;
	}

	/**
	 * Tests if the client has a specific permission.
	 * 
	 * @param permission
	 *            The permission to test
	 * @return true if the client has the permission.
	 */
	public boolean hasClientPermission(String permission) {
		return (client != null) ? client.hasPermission(permission) : false;
	}

	/**
	 * Tests if the user has delegated a specific permission to the client.
	 * 
	 * @param permission
	 *            The permission to test
	 * @return true if the permission is delegated.
	 */
	public boolean hasDelegatedPermission(String permission) {
		return (token != null) ? token.hasPermission(permission) : false;
	}

	/**
	 * Tests if the user has a specific claim type.
	 * 
	 * @param type
	 *            The claim type to test
	 * @return true if the user has the claim type.
	 */
	public boolean hasUserClaim(String type) {
		return (principal != null) ? principal.hasClaim(type) : false;
	}

	/**
	 * Tests if the user has a specific claim type and value.
	 * 
	 * @param type
	 *            The claim type to test
	 * @param value
	 *            The claim value to test
	 * @return true if the user has the claim type and value.
	 */
	public boolean hasUserClaim(String type, String value) {
		return (principal != null) ? principal.hasClaim(type, value) : false;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
