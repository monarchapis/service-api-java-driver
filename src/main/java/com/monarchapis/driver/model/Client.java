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

/**
 * Represents client details to be used in an user authorization application
 * such as OAuth.
 * 
 * @author Phil Kedy
 */
public class Client {
	/**
	 * The client identifier.
	 */
	private String id;

	/**
	 * The client API key.
	 */
	private String apiKey;

	/**
	 * The expiration in seconds for the token once granted.
	 */
	private Long expiration;

	/**
	 * Flag that denotes if the authorization application should automatically
	 * grant the client the requested privileges without prompting the end user.
	 * This is typically set to true if the client is developed by your
	 * organization or a well trusted third party.
	 */
	private boolean autoAuthorize;

	/**
	 * Flag that denotes if showing the authorization application in a native
	 * mobile control is permitted. (e.g. UIWebView for iOS or WebView for
	 * Android) Doing this could expose your customers credentials to a key
	 * stroke tracking attack.
	 */
	private boolean allowWebView;

	/**
	 * Flag that denotes if showing the authorization application in a popup
	 * window or IFrame is permitted. Doing this could expose your customers
	 * credentials to a key stroke tracking attack.
	 */
	private boolean allowPopup;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	public boolean isAutoAuthorize() {
		return autoAuthorize;
	}

	public void setAutoAuthorize(boolean autoAuthorize) {
		this.autoAuthorize = autoAuthorize;
	}

	public boolean isAllowWebView() {
		return allowWebView;
	}

	public void setAllowWebView(boolean allowWebViews) {
		this.allowWebView = allowWebViews;
	}

	public boolean isAllowPopup() {
		return allowPopup;
	}

	public void setAllowPopup(boolean allowPopup) {
		this.allowPopup = allowPopup;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
