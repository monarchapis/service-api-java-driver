package com.monarchapis.driver.jaxrs.v2;

import static org.mockito.Mockito.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;

@RunWith(MockitoJUnitRunner.class)
public class BadRequestExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		BadRequestExceptionMapper mapper = new BadRequestExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("badRequest", null)).thenReturn(error);
		BadRequestException e = Mockito.mock(BadRequestException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
