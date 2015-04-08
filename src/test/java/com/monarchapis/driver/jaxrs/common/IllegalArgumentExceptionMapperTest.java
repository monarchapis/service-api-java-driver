package com.monarchapis.driver.jaxrs.common;

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;

@RunWith(MockitoJUnitRunner.class)
public class IllegalArgumentExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		IllegalArgumentExceptionMapper mapper = new IllegalArgumentExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("invalidParameter", null, new Object[] { "test" })).thenReturn(error);
		Response response = mapper.toResponse(new IllegalArgumentException("test"));
		assertResponse(error, response);
	}
}
