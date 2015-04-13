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

package com.monarchapis.driver.util;

import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.PrincipalContext;

/**
 * Provides helper methods for pulling nested properties from the API context.
 * 
 * @author Phil Kedy
 */
public abstract class ApiContextUtils {
	/**
	 * Gets the user identifier.
	 * 
	 * @return the user identifier.
	 */
	public static String getUserId() {
		return getUserId(null);
	}

	/**
	 * Gets the user identifier with a default value.
	 * 
	 * @param defaultUserId
	 *            The default user identifier.
	 * @return The real user identifier if found, the default value otherwise.
	 */
	public static String getUserId(String defaultUserId) {
		PrincipalContext principal = getPrincipal();
		String userId = defaultUserId;

		if (principal != null) {
			userId = principal.getId();
		}

		return userId;
	}

	/**
	 * Tests if the principal has a claim of a specific type.
	 * 
	 * @param type
	 *            The claim type.
	 * @return true if the claim type was found.
	 */
	public static boolean hasClaim(String type) {
		PrincipalContext principal = getPrincipal();
		boolean exists = false;

		if (principal != null) {
			exists = principal.hasClaim(type);
		}

		return exists;
	}

	/**
	 * Tests if the principal has a claim of a specific type and value.
	 * 
	 * @param type
	 *            The claim type
	 * @param value
	 *            The claim value
	 * @return true if the claim type and value was found.
	 */
	public static boolean hasClaim(String type, String value) {
		PrincipalContext principal = getPrincipal();
		boolean exists = false;

		if (principal != null) {
			exists = principal.hasClaim(type, value);
		}

		return exists;
	}

	/**
	 * Returns the current principal.
	 * 
	 * @return the principal if found, null otherwise.
	 */
	public static PrincipalContext getPrincipal() {
		ApiContext context = ApiContext.getCurrent();
		PrincipalContext principal = null;

		if (context != null) {
			principal = context.getPrincipal();
		}

		return principal;
	}
}
