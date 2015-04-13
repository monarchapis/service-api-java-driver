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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.PrincipalContext;

public class ApiContextUtilsTest {
	private static ApiContext emptyContext;

	private static ApiContext fullContext;

	private static PrincipalContext principal;

	static {
		emptyContext = new ApiContext();

		Map<String, Set<String>> claims = new HashMap<String, Set<String>>();
		claims.put("key", Sets.newHashSet("value"));
		principal = new PrincipalContext();
		principal.setId("test principal id");
		principal.setClaims(claims);

		fullContext = new ApiContext();
		fullContext.setPrincipal(principal);
	}

	@BeforeClass
	public static void initialize() {
		ApiContext.setAutoCreate(false);
	}

	@AfterClass
	public static void destroy() {
		ApiContext.setAutoCreate(true);
	}

	@Before
	@After
	public void setup() {
		ApiContext.remove();
	}

	@Test
	public void testGetPrincipal() {
		assertNull(ApiContextUtils.getPrincipal());

		ApiContext.setCurrent(emptyContext);
		assertNull(ApiContextUtils.getPrincipal());

		ApiContext.setCurrent(fullContext);
		assertSame(principal, ApiContextUtils.getPrincipal());
	}

	@Test
	public void testGetUserId() {
		assertNull(ApiContextUtils.getUserId());

		ApiContext.setCurrent(emptyContext);
		assertNull(ApiContextUtils.getUserId());

		ApiContext.setCurrent(fullContext);
		assertSame("test principal id", ApiContextUtils.getUserId());
	}

	@Test
	public void testHasClaim() {
		assertFalse(ApiContextUtils.hasClaim("key"));
		assertFalse(ApiContextUtils.hasClaim("key", "value"));

		ApiContext.setCurrent(emptyContext);
		assertFalse(ApiContextUtils.hasClaim("key"));
		assertFalse(ApiContextUtils.hasClaim("key", "value"));

		ApiContext.setCurrent(fullContext);
		assertTrue(ApiContextUtils.hasClaim("key"));
		assertTrue(ApiContextUtils.hasClaim("key", "value"));
	}
}
