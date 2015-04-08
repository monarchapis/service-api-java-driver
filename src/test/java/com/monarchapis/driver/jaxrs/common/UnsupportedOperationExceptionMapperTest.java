package com.monarchapis.driver.jaxrs.common;

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;

@RunWith(MockitoJUnitRunner.class)
public class UnsupportedOperationExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		UnsupportedOperationExceptionMapper mapper = new UnsupportedOperationExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("methodNotAllowed", null, new Object[0])).thenReturn(error);
		Response response = mapper.toResponse(new UnsupportedOperationException());
		assertResponse(error, response);
	}
}
