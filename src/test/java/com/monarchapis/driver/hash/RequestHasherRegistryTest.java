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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiException;

@RunWith(MockitoJUnitRunner.class)
public class RequestHasherRegistryTest {
	@Mock
	private RequestHasher hasher;

	private RequestHasherRegistry registry;

	@Before
	public void setup() {
		when(hasher.getName()).thenReturn("test");
		registry = new RequestHasherRegistry(hasher);
	}

	@Test
	public void testGetAllHashers() {
		List<RequestHasher> hashers = registry.getRequestHashers();
		assertNotNull(hashers);
		assertEquals(1, hashers.size());
		assertEquals(hasher, hashers.get(0));
	}

	@Test
	public void testGetKnownHasher() {
		assertEquals(hasher, registry.getRequestHasher("test"));
	}

	@Test
	public void testGetUnknownHasher() {
		try {
			registry.getRequestHasher("unknown");
			fail("This should have thrown an exception");
		} catch (ApiException apie) {
			assertEquals("Request hasher unknown is not registered", apie.getMessage());
		}
	}
}
