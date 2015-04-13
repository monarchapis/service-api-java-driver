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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.container.ContainerRequestContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizeRequestFilterTest {
	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	private Authenticator authenticator;

	@Mock
	private ContainerRequestContext context;

	@Mock
	private Claim claim;

	private AuthorizeRequestFilter filter;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.required(Authenticator.class)).thenReturn(authenticator);

		filter = new AuthorizeRequestFilter(//
				new String[] { "client" }, //
				true, //
				new String[] { "delegated" }, //
				new Claim[] { claim }, //
				new BigDecimal("1"));
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testGetters() {
		assertEquals("client", filter.getClient()[0]);
		assertEquals("delegated", filter.getDelegated()[0]);
		assertEquals(claim, filter.getClaims()[0]);
		assertEquals(true, filter.isUser());
		assertEquals(new BigDecimal("1"), filter.getRequestWeight());
	}

	@Test
	public void testAuthorization() throws IOException {
		filter.filter(context);
		verify(authenticator).performAccessChecks(new BigDecimal("1"), //
				new String[] { "client" }, //
				new String[] { "delegated" }, //
				true, //
				new Claim[] { claim });
	}
}
