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

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the common properties between {@link Token} and
 * {@link TokenRequest}.
 * 
 * @author Phil Kedy
 */
public class TokenBase {
	/**
	 * The grant type;
	 */
	private String grantType;

	/**
	 * The token type (e.g. Bearer)
	 */
	private String tokenType;

	/**
	 * The token's permissions typically granted by an end user via an
	 * authorization application such as OAuth.
	 */
	private Set<String> permissions;

	/**
	 * The token state.
	 */
	private String state;

	/**
	 * The token callback URI.
	 */
	private String uri;

	/**
	 * The token's associated user identifier.
	 */
	private String userId;

	/**
	 * The token's optional user context (e.g. user name instead of user id)
	 */
	private String userContext;

	/**
	 * The token's extended information. This can hold session ids to other
	 * backend systems, for example.
	 */
	private Map<String, Object> extended;

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserContext() {
		return userContext;
	}

	public void setUserContext(String userContext) {
		this.userContext = userContext;
	}

	public Map<String, Object> getExtended() {
		return extended;
	}

	public void setExtended(Map<String, Object> extended) {
		this.extended = extended;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
