package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.util.ErrorUtils;

@Provider
public class UnsupportedOperationExceptionMapper implements ExceptionMapper<UnsupportedOperationException> {
	@Override
	public Response toResponse(UnsupportedOperationException exception) {
		return Response
				.status(Response.Status.NOT_IMPLEMENTED)
				.entity(new ApiError(Response.Status.NOT_IMPLEMENTED.getStatusCode(), "notImplemented",
						"This method is not implemented", "Make sure the value you are sending is valid.", "BAD-0001",
						null)).type(ErrorUtils.getBestMediaType()).build();
	}
}