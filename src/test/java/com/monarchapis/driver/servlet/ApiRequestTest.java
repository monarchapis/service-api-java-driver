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

package com.monarchapis.driver.servlet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationSettings;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class ApiRequestTest {
	@Mock
	private ServiceResolver serviceResolver;

	@Mock
	private AuthenticationSettings authenticationSettings;

	@Mock
	private HttpServletRequest request;

	private ApiRequest apiRequest;

	@Before
	public void setup() throws IOException {
		when(serviceResolver.required(AuthenticationSettings.class)).thenReturn(authenticationSettings);
		authenticationSettings.setDelegateAuthorization(false);
		authenticationSettings.setBypassRateLimiting(false);
		ServiceResolver.setInstance(serviceResolver);

		when(request.getInputStream()).thenAnswer(new Answer<ServletInputStream>() {
			@Override
			public ServletInputStream answer(InvocationOnMock invocation) throws Throwable {
				return new ServletInputStreamWrapper("This is a test".getBytes());
			}
		});
		when(request.getProtocol()).thenReturn("http");
		when(request.getServerName()).thenReturn("localhost");
		when(request.getServerPort()).thenReturn(8080);
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getQueryString()).thenReturn("a=1&b=2");
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(request.getMethod()).thenReturn("POST");
		when(request.getRequestURL()).thenAnswer(new Answer<StringBuffer>() {
			@Override
			public StringBuffer answer(InvocationOnMock invocation) throws Throwable {
				return new StringBuffer("/test");
			}
		});

		final Vector<String> headerNames = new Vector<String>();
		headerNames.add("name1");
		headerNames.add("name2");
		when(request.getHeaderNames()).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock invocation) throws Throwable {
				return headerNames.elements();
			}
		});
		when(request.getHeaders(anyString())).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock invocation) throws Throwable {
				String name = invocation.getArgumentAt(0, String.class);
				Vector<String> headerValues = new Vector<String>();
				headerValues.add(name + "-value1");
				headerValues.add(name + "-value2");
				return headerValues.elements();
			}
		});

		apiRequest = new ApiRequest(request);
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testThreadLocal() {
		ApiRequest request = Mockito.mock(ApiRequest.class);
		assertNull(ApiRequest.getCurrent());
		ApiRequest.setCurrent(request);
		assertSame(request, ApiRequest.getCurrent());
		ApiRequest.remove();
		assertNull(ApiRequest.getCurrent());
		ApiRequest.setCurrent(request);
		ApiRequest.setCurrent(null);
		assertNull(ApiRequest.getCurrent());
	}

	@Test
	public void testRequestId() throws IOException {
		ApiRequest request1 = new ApiRequest(request);
		ApiRequest request2 = new ApiRequest(request);

		assertNotEquals(request1.getRequestId(), request2.getRequestId());
	}

	@Test
	public void testGetInputStream() throws IOException {
		InputStream is = apiRequest.getInputStream();
		String actual = IOUtils.toString(is);
		IOUtils.closeQuietly(is);
		assertEquals("This is a test", actual);
	}

	@Test
	public void testGetBody() {
		assertEquals("This is a test", new String(apiRequest.getBody()));
	}

	@Test
	public void testGetFullURL() {
		when(request.getQueryString()).thenReturn(null);
		assertEquals("/test", apiRequest.getFullURL());
		when(request.getQueryString()).thenReturn("a=1&b=2");
		assertEquals("/test?a=1&b=2", apiRequest.getFullURL());
	}

	@Test
	public void testGetHeaderMap() {
		Map<String, List<String>> headers = apiRequest.getHeaderMap();
		assertEquals(Lists.newArrayList("name1-value1", "name1-value2"), headers.get("name1"));
		assertEquals(Lists.newArrayList("name2-value1", "name2-value2"), headers.get("name2"));
		assertNull(headers.get("doesnotexist"));
	}

	@Test
	public void getIpAddress() {
		assertEquals("127.0.0.1", apiRequest.getIpAddress());
		when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1");
		assertEquals("10.0.0.1", apiRequest.getIpAddress());
		when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1 , 10.0.0.2");
		assertEquals("10.0.0.1", apiRequest.getIpAddress());
	}

	@Test
	public void testCreateAuthorizationRequest() {
		AuthenticationRequest request = apiRequest.createAuthorizationRequest();
		assertEquals("http", request.getProtocol());
		assertEquals("localhost", request.getHost());
		assertEquals(8080, request.getPort());
		assertEquals("/test", request.getPath());
		assertEquals("a=1&b=2", request.getQuerystring());
		assertEquals("127.0.0.1", request.getIpAddress());
		Map<String, List<String>> headers = request.getHeaders();
		assertEquals(Lists.newArrayList("name1-value1", "name1-value2"), headers.get("name1"));
		assertEquals(Lists.newArrayList("name2-value1", "name2-value2"), headers.get("name2"));
		assertNull(headers.get("doesnotexist"));
	}

	@Test
	public void testGetDataSize() {
		assertEquals(123, apiRequest.getDataSize());
	}
}
