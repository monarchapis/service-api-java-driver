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

package com.monarchapis.driver.analytics;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.monarchapis.api.v1.client.AnalyticsApi;
import com.monarchapis.api.v1.client.EventsResource;
import com.monarchapis.api.v1.model.ObjectData;
import com.monarchapis.api.v1.model.Reference;
import com.monarchapis.api.v1.model.ServiceInfo;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.model.ApplicationContext;
import com.monarchapis.driver.model.ClaimNames;
import com.monarchapis.driver.model.Claims;
import com.monarchapis.driver.model.ClaimsHolder;
import com.monarchapis.driver.model.ClientContext;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.PrincipalContext;
import com.monarchapis.driver.model.TokenContext;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

@RunWith(MockitoJUnitRunner.class)
public class MonarchV1AnalyticsHandlerTest {
	private ObjectData objectData;

	@Mock
	private AnalyticsApi analyticsApi;

	@Mock
	private EventsResource eventsResource;

	@Mock
	private ServiceInfoResolver serviceInfoResolver;

	@Mock
	private ApiRequest request;

	@Mock
	private ApiResponse response;

	@InjectMocks
	private MonarchV1AnalyticsHandler handler;

	@Mock
	private Claims claims;

	@Before
	public void setup() {
		OperationNameHolder.remove();
		ErrorHolder.remove();
		ClaimsHolder.remove();
		objectData = null;

		Reference reference = new Reference();
		reference.setId("test");
		reference.setName("test");
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setService(Optional.of(reference));
		serviceInfo.setProvider(Optional.of(reference));
		when(serviceInfoResolver.getServiceInfo("/test")).thenReturn(serviceInfo);

		final Vector<String> headerNames = new Vector<String>();
		headerNames.add("test1");
		headerNames.add("test2");
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getHeaderNames()).thenAnswer(new Answer<Enumeration<String>>() {
			@Override
			public Enumeration<String> answer(InvocationOnMock invocation) throws Throwable {
				return headerNames.elements();
			}
		});
		when(request.getHeader("test1")).thenReturn("value1");
		when(request.getHeader("test2")).thenReturn("value2");

		when(analyticsApi.getEventsResource()).thenReturn(eventsResource);

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				MonarchV1AnalyticsHandlerTest.this.objectData = invocation.getArgumentAt(1, ObjectData.class);
				return null;
			}
		}).when(eventsResource).collectEvent(anyString(), any(ObjectData.class));
	}

	@After
	public void cleanup() {
		OperationNameHolder.remove();
		ClaimsHolder.remove();
	}

	@Test
	public void testReturnWhenAnyPrerequisiteIsNull() {
		MonarchV1AnalyticsHandler handler = new MonarchV1AnalyticsHandler();

		ClaimsHolder.setAutoCreate(false);
		handler.collect(request, response, 100);
		verify(eventsResource, never()).collectEvent(anyString(), any(ObjectData.class));

		ClaimsHolder.setAutoCreate(true);
		handler.collect(request, response, 100);
		verify(eventsResource, never()).collectEvent(anyString(), any(ObjectData.class));

		handler.setAnalyticsApi(analyticsApi);
		handler.collect(request, response, 100);
		verify(eventsResource, never()).collectEvent(anyString(), any(ObjectData.class));
	}

	@Test
	public void testOperationNameIsSetToUnknownWhenNull() {
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		assertEquals("unknown", objectData.get("operation_name"));

		OperationNameHolder.setCurrent("test");
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		assertEquals("test", objectData.get("operation_name"));
	}

	@Test
	public void testWillUseReferenceIdsIfSet() {
		ApplicationContext applicationContext = mock(ApplicationContext.class);
		when(applicationContext.getId()).thenReturn("test app id");

		ClientContext clientContext = mock(ClientContext.class);
		when(clientContext.getId()).thenReturn("test client id");

		TokenContext tokenContext = mock(TokenContext.class);
		when(tokenContext.getId()).thenReturn("test token id");

		PrincipalContext principalContext = mock(PrincipalContext.class);
		when(principalContext.getId()).thenReturn("test principal id");

		when(claims.getAs(ApplicationContext.class, ClaimNames.APPLICATION))
				.thenReturn(Optional.of(applicationContext));
		when(claims.getAs(ClientContext.class, ClaimNames.CLIENT)).thenReturn(Optional.of(clientContext));
		when(claims.getAs(TokenContext.class, ClaimNames.TOKEN)).thenReturn(Optional.of(tokenContext));
		when(claims.getAs(PrincipalContext.class, ClaimNames.PRINCIPAL)).thenReturn(Optional.of(principalContext));
		ClaimsHolder.setCurrent(claims);

		handler.collect(request, response, 100);

		assertNotNull(objectData);
		assertEquals("test app id", objectData.get("application_id"));
		assertEquals("test client id", objectData.get("client_id"));
		assertEquals("test token id", objectData.get("token_id"));
		assertEquals("test principal id", objectData.get("user_id"));
	}

	@Test
	public void testWillUseTheApiErrorStatusIfSet() {
		when(response.getStatus()).thenReturn(200);
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		assertEquals(200, objectData.get("status_code"));
		assertEquals("ok", objectData.get("error_reason"));

		ApiError error = mock(ApiError.class);
		when(error.getStatus()).thenReturn(4321);
		when(error.getReason()).thenReturn("test");
		ErrorHolder.setCurrent(error);
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		assertEquals(4321, objectData.get("status_code"));
		assertEquals("test", objectData.get("error_reason"));
	}

	@Test
	public void testWillParseTheQueryString() {
		when(request.getQueryString()).thenReturn("test1=value%201&test2=value%202&test3");
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		ObjectNode parameters = (ObjectNode) objectData.get("parameters");
		assertEquals("value 1", parameters.path("test1").asText());
		assertEquals("value 2", parameters.path("test2").asText());
	}

	@Test
	public void testWillIgnoreURLDecodingExceptions() throws UnsupportedEncodingException {
		// Not working
		// PowerMockito.mockStatic(URLDecoder.class);
		// PowerMockito.when(URLDecoder.decode("value%201",
		// "UTF-8")).thenThrow(new UnsupportedEncodingException("test"));
		//
		// when(request.getQueryString()).thenReturn("test1=value%201&test2=value%202&test3");
		// handler.collect(request, response, 100);
	}

	@Test
	public void testWillParseRequestHeaders() {
		handler.collect(request, response, 100);

		assertNotNull(objectData);
		ObjectNode headers = (ObjectNode) objectData.get("headers");
		assertEquals("value1", headers.path("test1").asText());
		assertEquals("value2", headers.path("test2").asText());
	}
}
