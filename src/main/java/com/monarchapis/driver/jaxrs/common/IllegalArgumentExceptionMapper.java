package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.util.ErrorUtils;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
	@Override
	public Response toResponse(IllegalArgumentException exception) {
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(new ApiError(Response.Status.BAD_REQUEST.getStatusCode(), "illegalArgument", exception
						.getMessage(), "Make sure the value you are sending is valid.", "BAD-0001", null))
				.type(ErrorUtils.getBestMediaType()).build();
	}
}