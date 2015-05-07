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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Store provider credential settings for the API.
 * 
 * @author Phil Kedy
 */
//public class ProviderCredentials {
//	/**
//	 * The API key.
//	 */
//	private String apiKey;
//
//	/**
//	 * The shared secret.
//	 */
//	private String sharedSecret;
//
//	public ProviderCredentials() {
//	}
//
//	public ProviderCredentials(String apiKeyt) {
//		setApiKey(apiKey);
//	}
//
//	public ProviderCredentials(String apiKey, String shareSecret) {
//		setApiKey(apiKey);
//		this.sharedSecret = shareSecret;
//	}
//
//	public String getApiKey() {
//		return apiKey;
//	}
//
//	public void setApiKey(String apiKey) {
//		Validate.notBlank(apiKey, "apiKey is required.");
//		this.apiKey = apiKey;
//	}
//
//	public String getSharedSecret() {
//		return sharedSecret;
//	}
//
//	public void setSharedSecret(String sharedSecret) {
//		this.sharedSecret = sharedSecret;
//	}
//
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
//	}
//}
