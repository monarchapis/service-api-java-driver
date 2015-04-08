package com.monarchapis.driver.jaxrs.jersey1;

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;
import com.sun.jersey.api.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class NotFoundExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
		ApiError error = new ApiError(404, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("notFound", null)).thenReturn(error);
		NotFoundException e = Mockito.mock(NotFoundException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
