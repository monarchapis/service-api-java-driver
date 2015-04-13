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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a granted application after the user has allowed the requested
 * access via an authorization application such as OAuth.
 * 
 * @author Phil Kedy
 */
public class GrantedApplication extends Application {
	/**
	 * The token identifier that was generated.
	 */
	private String tokenId;

	/**
	 * The permissions attached to the generated token.
	 */
	private Set<String> permissions;

	/**
	 * The permission messages to display back to the user as a confirmation.
	 */
	private List<Message> permissionMessages;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public List<Message> getPermissionMessages() {
		return permissionMessages;
	}

	public void setPermissionMessages(List<Message> permissionMessages) {
		this.permissionMessages = permissionMessages;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
