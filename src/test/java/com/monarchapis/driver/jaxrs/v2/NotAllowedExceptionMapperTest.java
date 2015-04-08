package com.monarchapis.driver.jaxrs.v2;

import static org.mockito.Mockito.*;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;

@RunWith(MockitoJUnitRunner.class)
public class NotAllowedExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		NotAllowedExceptionMapper mapper = new NotAllowedExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("methodNotAllowed", null)).thenReturn(error);
		NotAllowedException e = Mockito.mock(NotAllowedException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
