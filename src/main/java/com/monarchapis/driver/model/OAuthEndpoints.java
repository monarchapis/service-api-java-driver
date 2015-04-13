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

/**
 * Represents settings to an API's OAuth 2.0 endpoints. Mainly used for
 * injecting the URLs into API dynamically generated documentation such as
 * Swagger.
 * 
 * @author Phil Kedy
 */
public class OAuthEndpoints {
	/**
	 * The OAuth authorization code flow request URL.
	 */
	private String authorizationCodeRequestUrl;

	/**
	 * The OAuth authorization code flow token URL.
	 */
	private String authorizationCodeTokenUrl;

	/**
	 * The OAuth implicit flow URL.
	 */
	private String implicitUrl;

	/**
	 * The OAuth password flow URL.
	 */
	private String passwordUrl;

	public String getAuthorizationCodeRequestUrl() {
		return authorizationCodeRequestUrl;
	}

	public void setAuthorizationCodeRequestUrl(String authorizationCodeRequestUrl) {
		this.authorizationCodeRequestUrl = authorizationCodeRequestUrl;
	}

	public String getAuthorizationCodeTokenUrl() {
		return authorizationCodeTokenUrl;
	}

	public void setAuthorizationCodeTokenUrl(String authorizationCodeTokenUrl) {
		this.authorizationCodeTokenUrl = authorizationCodeTokenUrl;
	}

	public String getImplicitUrl() {
		return implicitUrl;
	}

	public void setImplicitUrl(String implicitUrl) {
		this.implicitUrl = implicitUrl;
	}

	public String getPasswordUrl() {
		return passwordUrl;
	}

	public void setPasswordUrl(String passwordUrl) {
		this.passwordUrl = passwordUrl;
	}
}
