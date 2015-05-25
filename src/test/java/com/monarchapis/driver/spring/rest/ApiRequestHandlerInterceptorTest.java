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

package com.monarchapis.driver.spring.rest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.method.HandlerMethod;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class ApiRequestHandlerInterceptorTest {
	@Mock
	private Authenticator authenticator;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@InjectMocks
	private ApiRequestHandlerInterceptor interceptor;

	@Mock
	private HandlerMethod handler;

	@Mock
	private ServiceResolver serviceResolver;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.required(Authenticator.class)).thenReturn(authenticator);
	}

	@After
	public void teardown() {
		OperationNameHolder.remove();
		VersionHolder.remove();
		BypassAnalyticsHolder.remove();
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testNoHandler() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(NoAnnotations.class));
		assertTrue(interceptor.preHandle(request, response, null));
	}

	// Will use the actual java method name when the ApiOperation annotation is
	// not present
	@Test
	public void testWithoutOperation() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(NoAnnotations.class));
		interceptor.preHandle(request, response, handler);
		assertEquals("someMethod", OperationNameHolder.getCurrent());
	}

	// Will use the value set in the ApiOperation annotation if present
	@Test
	public void testWithOperation() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(WithOperation.class));
		interceptor.preHandle(request, response, handler);
		assertEquals("test", OperationNameHolder.getCurrent());
	}

	@Test
	public void testVersionOnMethod() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(VersionOnMethod.class));
		interceptor.preHandle(request, response, handler);
		assertEquals("v1", VersionHolder.getCurrent());
	}

	@Test
	public void testVersionOnClass() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(VersionOnClass.class));
		interceptor.preHandle(request, response, handler);
		assertEquals("v1", VersionHolder.getCurrent());
	}

	@Test
	public void testBypassAnalyticsOnMethod() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(BypassAnalyticsOnMethod.class));
		interceptor.preHandle(request, response, handler);
		assertTrue(BypassAnalyticsHolder.getCurrent());
	}

	@Test
	public void testBypassAnalyticsOnClass() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(BypassAnalyticsOnClass.class));
		interceptor.preHandle(request, response, handler);
		assertTrue(BypassAnalyticsHolder.getCurrent());
	}

	@Test
	public void testAuthorizeOnMethod() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(AuthorizeOnMethod.class));
		interceptor.preHandle(request, response, handler);
		verify(authenticator).performAccessChecks(//
				eq(new BigDecimal("1")), //
				eq(new String[] { "client" }), //
				eq(new String[] { "delegated" }), //
				eq(true), //
				any(Claim[].class));
	}

	@Test
	public void testAuthorizeOnClass() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(AuthorizeOnClass.class));
		interceptor.preHandle(request, response, handler);
		verify(authenticator).performAccessChecks(//
				eq(new BigDecimal("1")), //
				eq(new String[] { "client" }), //
				eq(new String[] { "delegated" }), //
				eq(true), //
				any(Claim[].class));
	}

	@Test
	public void testRequestWeight() throws Exception {
		when(handler.getMethod()).thenReturn(getMethod(RequestWeightClass.class));
		interceptor.preHandle(request, response, handler);
		verify(authenticator).performAccessChecks(//
				eq(new BigDecimal("2")), //
				eq(new String[] { "client" }), //
				eq(new String[] { "delegated" }), //
				eq(true), //
				any(Claim[].class));
	}

	private Method getMethod(Class<?> clazz) {
		try {
			return clazz.getMethod("someMethod");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private static class NoAnnotations {
		public void someMethod() {
		}
	}

	private static class WithOperation {
		@ApiOperation("test")
		public void someMethod() {
		}
	}

	private static class VersionOnMethod {
		@ApiVersion("v1")
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@ApiVersion("v1")
	private static class VersionOnClass {
		public void someMethod() {
		}
	}

	private static class BypassAnalyticsOnMethod {
		@BypassAnalytics
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@BypassAnalytics
	private static class BypassAnalyticsOnClass {
		public void someMethod() {
		}
	}

	private static class AuthorizeOnMethod {
		@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
	private static class AuthorizeOnClass {
		public void someMethod() {
		}
	}

	private static class RequestWeightClass {
		@RequestWeight("2")
		@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
		public void someMethod() {
		}
	}
}
