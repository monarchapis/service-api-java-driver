package com.monarchapis.driver.jaxrs.common;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.servlet.ApiRequest;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseExceptionMapperTest {
	@Mock
	private ApiRequest request;

	private ApiError error;
	private ApiErrorException e;
	private ApiResponseExceptionMapper mapper;

	@Before
	public void setup() {
		ApiRequest.setCurrent(request);
		when(request.getHeader("Accept")).thenReturn("application/json");

		error = new ApiError(404, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		e = new ApiErrorException(error);
		mapper = new ApiResponseExceptionMapper();
	}

	@After
	public void teardown() {
		ApiRequest.remove();
	}

	@Test
	public void testToResponse() {
		Response response = mapper.toResponse(e);
		assertNotNull(response);
		assertEquals(error.getStatus(), response.getStatus());
		assertEquals(error, response.getEntity());
	}
}
