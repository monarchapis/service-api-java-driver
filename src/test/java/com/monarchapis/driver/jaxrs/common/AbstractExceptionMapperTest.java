package com.monarchapis.driver.jaxrs.common;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.util.ServiceResolver;

public class AbstractExceptionMapperTest {
	@Mock
	protected ApiRequest request;

	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	protected ApiErrorFactory apiErrorFactory;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.getService(ApiErrorFactory.class)).thenReturn(apiErrorFactory);

		ApiRequest.setCurrent(request);
		when(request.getHeader("Accept")).thenReturn("application/json");
	}

	@After
	public void teardown() {
		ApiRequest.remove();
		ErrorHolder.remove();
		ServiceResolver.setInstance(null);
	}

	protected void assertResponse(ApiError error, Response response) {
		assertNotNull(response);
		assertEquals(error.getStatus(), response.getStatus());
		ApiError responseError = (ApiError) response.getEntity();
		assertEquals(error, responseError);
	}
}
