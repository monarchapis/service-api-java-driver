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

package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.jaxrs.common.AbstractAuthorizeFilter;

/**
 * Checks the permissions of the incoming API request.
 * 
 * @author Phil Kedy
 */
@Priority(Priorities.AUTHORIZATION)
public class AuthorizeRequestFilter extends AbstractAuthorizeFilter implements ContainerRequestFilter {
	public AuthorizeRequestFilter(String[] client, boolean user, String[] delegated, Claim[] claims,
			BigDecimal requestWeight) {
		super(client, user, delegated, claims, requestWeight);
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		checkPermissions();
	}
}