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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents principal context details returned by the request authentication
 * call. These fields can be used for authorization or custom logic in your API
 * code.
 * 
 * @author Phil Kedy
 */
public class PrincipalContext {
	/**
	 * The principal identifier.
	 */
	private String id;

	/**
	 * The principal context (e.g. user name instead of user id).
	 */
	private String context;

	/**
	 * The claims that the user/principal have for security assertion.
	 */
	private Map<String, Set<String>> claims;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Map<String, Set<String>> getClaims() {
		return claims;
	}

	public void setClaims(Map<String, Set<String>> claims) {
		this.claims = claims;
	}

	/**
	 * A helper method that determines if the principal has a specific claim
	 * type.
	 * 
	 * @param claimType
	 *            The claim type.
	 * @return true if the principal has the claim type.
	 */
	public boolean hasClaim(String claimType) {
		return hasClaim(claimType, null);
	}

	/**
	 * A helper method that determines if the principal has a specific claim
	 * type and value.
	 * 
	 * @param claimType
	 *            The claim type.
	 * @param claimValue
	 *            The claim value.
	 * @return true if the principal has the claim type.
	 */
	public boolean hasClaim(String claimType, String claimValue) {
		if (claims == null) {
			return false;
		}

		Set<String> values = claims.get(claimType);

		if (values == null) {
			return false;
		}

		return StringUtils.isEmpty(claimValue) || values.contains(claimValue);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
