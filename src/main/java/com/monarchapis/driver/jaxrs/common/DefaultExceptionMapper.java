package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.util.ErrorUtils;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	@Override
	public Response toResponse(Exception exception) {
		logger.error("An uncaught exception occurred", exception);

		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApiError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "systemError",
						"A general error has occurred.", "A general error has occurred.", "SYSTEM-0001", null))
				.type(ErrorUtils.getBestMediaType()).build();
	}
}