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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CorsFilterTest {
	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterConfig config;

	@Mock
	private FilterChain chain;

	private CorsFilter filter;

	@Before
	public void setup() throws ServletException {
		filter = new CorsFilter();
		when(config.getInitParameter("allowMethods")).thenReturn("GET, POST");
		when(config.getInitParameter("allowHeaders")).thenReturn("Content-Type, Accept, Origin");

		filter.init(config);
	}

	@After
	public void teardown() {
		filter.destroy();
	}

	@Test
	public void testNoOrigin() throws IOException, ServletException {
		when(request.getHeader("Origin")).thenReturn(null);

		when(request.getMethod()).thenReturn("GET");
		filter.doFilter(request, response, chain);
		verify(chain).doFilter(request, response);
		verify(response, never()).setHeader(eq("Allow"), anyString());
		verify(response, never()).setStatus(200);

		reset(chain, response);

		when(request.getMethod()).thenReturn("OPTIONS");
		filter.doFilter(request, response, chain);
		verify(chain, never()).doFilter(request, response);
		verify(response).setHeader(eq("Allow"), anyString());
		verify(response).setStatus(200);
	}

	@Test
	public void testWildcardOrigins() throws IOException, ServletException {
		when(request.getMethod()).thenReturn("OPTIONS");
		when(request.getHeader("Origin")).thenReturn("localhost:8080");
		filter.doFilter(request, response, chain);
		verify(response).setHeader("Access-Control-Allow-Origin", "localhost:8080");
		verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST");
		verify(response).setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Origin");
		verify(response).setHeader("Access-Control-Max-Age", "1728000");
		verify(response).addHeader("Access-Control-Allow-Credentials", "true");
	}

	@Test
	public void testSpecificOrigins() throws IOException, ServletException {
		when(config.getInitParameter("allowOrigins")).thenReturn("localhost:80");

		filter.init(config);

		when(request.getMethod()).thenReturn("OPTIONS");
		when(request.getHeader("Origin")).thenReturn("localhost:8080");
		filter.doFilter(request, response, chain);
		verify(response).setHeader("Access-Control-Allow-Origin", "localhost:80");
		verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST");
		verify(response).setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Origin");
		verify(response).setHeader("Access-Control-Max-Age", "1728000");
		verify(response).addHeader("Access-Control-Allow-Credentials", "true");
	}
}
