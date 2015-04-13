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
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.servlet.ApiRequest;

@RunWith(MockitoJUnitRunner.class)
public class MediaTypeUtilsTest {
	@Mock
	private ApiRequest request;

	@Before
	public void setup() {
		ApiRequest.setCurrent(request);
	}

	@After
	public void teardown() {
		ApiRequest.remove();
	}

	@Test
	public void testWithNoAccept() {
		when(request.getHeader("Accept")).thenReturn(null);
		String actual = MediaTypeUtils.getBestMediaType();
		assertEquals("application/json", actual);
	}

	@Test
	public void testWithAcceptJSON() {
		when(request.getHeader("Accept")).thenReturn("application/json;q=1.0,application/xml;q=0.8");
		String actual = MediaTypeUtils.getBestMediaType();
		assertEquals("application/json", actual);
	}

	@Test
	public void testWithAcceptXML() {
		when(request.getHeader("Accept")).thenReturn("application/json;q=0.8,application/xml;q=1.0");
		String actual = MediaTypeUtils.getBestMediaType();
		assertEquals("application/xml", actual);
	}

	@Test
	public void testWithAcceptUnsupported() {
		when(request.getHeader("Accept")).thenReturn("application/abc;q=0.8,application/def;q=1.0");
		String actual = MediaTypeUtils.getBestMediaType();
		assertEquals("application/json", actual);
	}
}
