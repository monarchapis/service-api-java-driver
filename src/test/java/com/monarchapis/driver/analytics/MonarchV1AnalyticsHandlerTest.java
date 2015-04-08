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
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.ApplicationContext;
import com.monarchapis.driver.model.ClientContext;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.PrincipalContext;
import com.monarchapis.driver.model.Reference;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.model.TokenContext;
import com.monarchapis.driver.service.v1.AnalyticsApi;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

@RunWith(MockitoJUnitRunner.class)
public class MonarchV1AnalyticsHandlerTest {
	private ObjectNode objectNode;

	@Mock
	private AnalyticsApi analyticsApi;

	@Mock
	private ServiceInfoResolver serviceInfoResolver;

	@Mock
	private ApiRequest request;

	@Mock
	private ApiResponse response;

	@InjectMocks
	private MonarchV1AnalyticsHandler handler;

	@Mock
	private ApiContext apiContext;

	@Before
	public void setup() {
		OperationNameHolder.remove();
		ErrorHolder.remove();
		ApiContext.remove();
		objectNode = null;

		Reference reference = new Reference();
		reference.setId("test");
		reference.setName("test");
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setService(reference);
		serviceInfo.setProvider(reference);
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

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				MonarchV1AnalyticsHandlerTest.this.objectNode = invocation.getArgumentAt(1, ObjectNode.class);
				return null;
			}

		}).when(analyticsApi).event(anyString(), any(ObjectNode.class));
	}
	
	@After
	public void cleanup() {
		OperationNameHolder.remove();
		ApiContext.remove();
	}

	@Test
	public void testReturnWhenAnyPrerequisiteIsNull() {
		MonarchV1AnalyticsHandler handler = new MonarchV1AnalyticsHandler();

		ApiContext.setAutoCreate(false);
		handler.collect(request, response, 100);
		verify(analyticsApi, never()).event(anyString(), any(ObjectNode.class));

		ApiContext.setAutoCreate(true);
		handler.collect(request, response, 100);
		verify(analyticsApi, never()).event(anyString(), any(ObjectNode.class));

		handler.setAnalyticsApi(analyticsApi);
		handler.collect(request, response, 100);
		verify(analyticsApi, never()).event(anyString(), any(ObjectNode.class));
	}

	@Test
	public void testOperationNameIsSetToUnknownWhenNull() {
		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals("unknown", objectNode.path("operation_name").asText());

		OperationNameHolder.setCurrent("test");
		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals("test", objectNode.path("operation_name").asText());
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

		when(apiContext.getApplication()).thenReturn(applicationContext);
		when(apiContext.getClient()).thenReturn(clientContext);
		when(apiContext.getToken()).thenReturn(tokenContext);
		when(apiContext.getPrincipal()).thenReturn(principalContext);
		ApiContext.setCurrent(apiContext);

		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals("test app id", objectNode.path("application_id").asText());
		assertEquals("test client id", objectNode.path("client_id").asText());
		assertEquals("test token id", objectNode.path("token_id").asText());
		assertEquals("test principal id", objectNode.path("user_id").asText());
	}

	@Test
	public void testWillUseTheApiErrorStatusIfSet() {
		when(response.getStatus()).thenReturn(200);
		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals(200, objectNode.path("status_code").asInt());
		assertEquals("ok", objectNode.path("error_reason").asText());

		ApiError error = mock(ApiError.class);
		when(error.getStatus()).thenReturn(4321);
		when(error.getReason()).thenReturn("test");
		ErrorHolder.setCurrent(error);
		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals(4321, objectNode.path("status_code").asInt());
		assertEquals("test", objectNode.path("error_reason").asText());
	}

	@Test
	public void testWillParseTheQueryString() {
		when(request.getQueryString()).thenReturn("test1=value%201&test2=value%202&test3");
		handler.collect(request, response, 100);

		assertNotNull(objectNode);
		assertEquals("value 1", objectNode.path("parameters").path("test1").asText());
		assertEquals("value 2", objectNode.path("parameters").path("test2").asText());
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

		assertNotNull(objectNode);
		assertEquals("value1", objectNode.path("headers").path("test1").asText());
		assertEquals("value2", objectNode.path("headers").path("test2").asText());
	}
}
