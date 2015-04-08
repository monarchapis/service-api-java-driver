package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.util.MediaTypeUtils;
import com.monarchapis.driver.util.ServiceResolver;

public abstract class AbstractExceptionMapper<T extends Exception> implements ExceptionMapper<T> {
	private static final Object[] NO_ARGUMENTS = new Object[0];

	@Override
	public Response toResponse(T exception) {
		ApiErrorFactory apiErrorFactory = ServiceResolver.getInstance().getService(ApiErrorFactory.class);

		ApiError error = apiErrorFactory.error(//
				getReason(exception), //
				getTemplate(exception), //
				getArguments(exception));
		overrideErrorProperties(error, exception);

		return Response //
				.status(Response.Status.fromStatusCode(error.getStatus())) //
				.entity(error) //
				.type(MediaTypeUtils.getBestMediaType()) //
				.build();
	}

	protected String getReason(T exception) {
		return getReason();
	}

	protected String getReason() {
		throw new UnsupportedOperationException("Please override getReason() or getReason(String)");
	}

	protected String getTemplate(T exception) {
		return null;
	}

	protected Object[] getArguments(T exception) {
		return NO_ARGUMENTS;
	}

	protected void overrideErrorProperties(ApiError error, T exception) {
	}
}