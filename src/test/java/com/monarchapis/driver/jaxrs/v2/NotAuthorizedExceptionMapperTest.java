package com.monarchapis.driver.jaxrs.v2;

import static org.mockito.Mockito.*;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;

@RunWith(MockitoJUnitRunner.class)
public class NotAuthorizedExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		NotAuthorizedExceptionMapper mapper = new NotAuthorizedExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("notAuthorized", null)).thenReturn(error);
		NotAuthorizedException e = Mockito.mock(NotAuthorizedException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
