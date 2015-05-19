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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.servlet.ApiRequest;

@RunWith(MockitoJUnitRunner.class)
public class HawkV1RequestHasherTest {
	@Mock
	private ApiRequest request;

	private HawkV1RequestHasher hasher;

	@Before
	public void setup() {
		hasher = new HawkV1RequestHasher();
		when(request.getContentType()).thenReturn("text/plain; utf-8");
		when(request.getBody()).thenReturn("Thank you for flying Hawk".getBytes());
	}

	@Test
	public void testGetName() {
		assertEquals("Hawk V1", hasher.getName());
	}

	@Test
	public void testGetRequestHash() {
		assertEquals("Yi9LfIIFRtBEPt74PVmbTF/xVAwPn7ub15ePICfgnuY=", hasher.getRequestHash(request, "sha256"));
	}

	@Test
	public void testErrorHandling() {
		try {
			hasher.getRequestHash(request, "unknown");
		} catch (ApiException e) {
			assertEquals("Could not generate request hash", e.getMessage());
		}
	}
}
