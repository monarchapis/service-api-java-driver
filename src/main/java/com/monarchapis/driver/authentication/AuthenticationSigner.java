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

package com.monarchapis.driver.authentication;

import com.google.api.client.http.HttpRequest;

/**
 * Provides a mechanism to sign the API request with credentials before it is
 * sent.
 * 
 * @author Phil Kedy
 */
public interface AuthenticationSigner {
	/**
	 * @param request
	 *            The HTTP request object to sign
	 * @param algorithm
	 *            The algorithm to use in signing
	 * @param apiKey
	 *            The client/provider API key
	 * @param sharedSecret
	 *            The client/provider shared secret
	 * @param accessToken
	 *            The user's access token (optional)
	 */
	public void signRequest(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken);
}
