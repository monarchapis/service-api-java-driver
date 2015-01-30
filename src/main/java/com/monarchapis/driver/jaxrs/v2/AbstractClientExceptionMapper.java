package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.util.ErrorUtils;

public abstract class AbstractClientExceptionMapper<T extends ClientErrorException> implements ExceptionMapper<T> {
	public abstract String getReason();

	public abstract String getDeveloperMessage();

	public abstract String getErrorCode();

	@Override
	public final Response toResponse(T exception) {
		String message = exception.getMessage();

		if (message == null) {
			message = getDeveloperMessage();
		}

		ApiError error = new ApiError(//
				exception.getResponse().getStatus(), //
				getReason(), //
				message, //
				getDeveloperMessage(), //
				getErrorCode(), //
				null);

		return Response //
				.status(Response.Status.fromStatusCode(error.getCode())) //
				.entity(error) //
				.type(ErrorUtils.getBestMediaType()) //
				.build();
	}
}