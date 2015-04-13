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

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Contains the additional properties beyond {@link TokenBase} for a token
 * request.
 * 
 * @author Phil Kedy
 */
public class TokenRequest extends TokenBase implements Serializable {
	private static final long serialVersionUID = 4570272326916068744L;

	/**
	 * The API key of the client requesting the token.
	 */
	private String apiKey;

	/**
	 * The authorization scheme the client is using to request the token. These
	 * are profiles that are set up in Monarch's admin console.
	 */
	private String authorizationScheme;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAuthorizationScheme() {
		return authorizationScheme;
	}

	public void setAuthorizationScheme(String authorizationScheme) {
		this.authorizationScheme = authorizationScheme;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
