package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.util.MediaTypeUtils;

@Provider
public class ApiResponseExceptionMapper implements ExceptionMapper<ApiErrorException> {
	@Override
	public Response toResponse(ApiErrorException e) {
		ApiError error = e.getError();

		return Response //
				.status(error.getStatus()) //
				.entity(error) //
				.type(MediaTypeUtils.getBestMediaType()) //
				.build();
	}
}