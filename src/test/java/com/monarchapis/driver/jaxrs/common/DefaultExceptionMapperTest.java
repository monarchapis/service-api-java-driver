package com.monarchapis.driver.jaxrs.common;

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		DefaultExceptionMapper mapper = new DefaultExceptionMapper();
		ApiError error = new ApiError(500, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("internalError", null)).thenReturn(error);
		Response response = mapper.toResponse(new RuntimeException("test"));
		assertResponse(error, response);
	}
}
