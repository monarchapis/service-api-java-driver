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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.monarchapis.driver.exception.ApiException;

/**
 * Provides utility methods related to hashing operations.
 * 
 * @author Phil Kedy
 */
public final class HasherUtils {
	/**
	 * The algorithm translation dictionary lookup.
	 */
	private static Map<String, String> algrithmLookup = new HashMap<String, String>();

	static {
		algrithmLookup.put("md5", "MD5");
		algrithmLookup.put("sha1", "SHA-1");
		algrithmLookup.put("sha256", "SHA-256");
		algrithmLookup.put("sha384", "SHA-384");
		algrithmLookup.put("sha512", "SHA-512");
	}

	private HasherUtils() {
	}

	/**
	 * Translates a simple algorithm name to the Java standard name.
	 * 
	 * @param algorithm
	 *            The simple algorithm name
	 * @return the Java algorithm name
	 */
	public static String getMessageDigestAlgorithm(String algorithm) {
		Validate.notBlank(algorithm, "algorithm is a required parameter.");

		String digestAlgorithm = algrithmLookup.get(algorithm.toLowerCase());

		if (digestAlgorithm == null) {
			throw new ApiException("Invalid algorithm " + algorithm);
		}

		return digestAlgorithm;
	}
}
