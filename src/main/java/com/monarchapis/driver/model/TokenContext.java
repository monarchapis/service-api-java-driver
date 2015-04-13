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
 * Represents token context details returned by the request authentication call.
 * These fields can be used for authorization or custom logic in your API code.
 * 
 * @author Phil Kedy
 */
public class TokenContext {
	/**
	 * The token identifier.
	 */
	private String id;

	/**
	 * The token permissions.
	 */
	private Set<String> permissions;

	/**
	 * The token's extended information.
	 */
	private Map<String, String> extended;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(String permission) {
		return permissions != null && permissions.contains(permission);
	}

	public Map<String, String> getExtended() {
		return extended;
	}

	public void setExtended(Map<String, String> extended) {
		this.extended = extended;
	}

	public String getExtended(String key) {
		if (extended == null) {
			return null;
		}

		return extended.get(key);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
