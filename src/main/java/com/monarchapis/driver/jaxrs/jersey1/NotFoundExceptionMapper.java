package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.util.ErrorUtils;
import com.sun.jersey.api.NotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
	@Override
	public Response toResponse(NotFoundException exception) {
		String message = exception.getMessage();

		if (message == null) {
			message = "The resource you requested was not found.";
		}

		ApiError error = new ApiError(//
				Response.Status.NOT_FOUND.getStatusCode(), //
				"notFound", //
				message, //
				"The resource you requested was not found.", //
				"CLIENT-0001", //
				null);

		return Response //
				.status(Response.Status.NOT_FOUND) //
				.entity(error) //
				.type(ErrorUtils.getBestMediaType()) //
				.build();
	}
}