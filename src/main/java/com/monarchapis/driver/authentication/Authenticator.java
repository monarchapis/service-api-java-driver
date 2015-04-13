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

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;

/**
 * The interface used to authenticate and authorize API requests.
 * 
 * @author Phil Kedy
 */
public interface Authenticator {
	public static final BigDecimal NORMAL_WEIGHT = new BigDecimal("1");

	/**
	 * @param requestWeight
	 *            The request weight to count for rate limiting
	 * @param client
	 *            The required client permissions.
	 * @param delegated
	 *            The required permissions delegated to the user.
	 * @param user
	 *            Flag that denotes that an authenticated user is required.
	 * @param claims
	 *            The claims required by the user.
	 */
	public void performAccessChecks(BigDecimal requestWeight, String[] client, String[] delegated, boolean user,
			Claim[] claims);
}
