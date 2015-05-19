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

package com.monarchapis.driver.hash;

import com.monarchapis.driver.servlet.ApiRequest;

/**
 * Handles creating a request hash for an API request.
 * 
 * @author Phil Kedy
 */
public interface RequestHasher {
	/**
	 * Gets the name of the hasher for lookup in the
	 * <code>RequestHasherRegistry</code>.
	 * 
	 * @return the name of the request hasher
	 */
	public String getName();

	/**
	 * Creates a hash of the API request as a string.
	 * 
	 * @param request
	 *            The API request
	 * @param algorithm
	 *            The simple algorithm name (e.g. md5, sha1, sha256, sha384,
	 *            sha512)
	 * @return the request hash represented as a string.
	 */
	public String getRequestHash(ApiRequest request, String algorithm);
}
