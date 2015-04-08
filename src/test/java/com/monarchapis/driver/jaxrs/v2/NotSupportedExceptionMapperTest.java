package com.monarchapis.driver.jaxrs.v2;

import static org.mockito.Mockito.*;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;

@RunWith(MockitoJUnitRunner.class)
public class NotSupportedExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		NotSupportedExceptionMapper mapper = new NotSupportedExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("mediaTypeNotSupported", null)).thenReturn(error);
		NotSupportedException e = Mockito.mock(NotSupportedException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
