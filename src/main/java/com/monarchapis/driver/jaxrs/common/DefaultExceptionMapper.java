package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper<Exception> {
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	@Override
	protected String getReason() {
		return "internalError";
	}
	
	@Override
	public Response toResponse(Exception exception) {
		logger.error("An uncaught exception occurred", exception);

		return super.toResponse(exception);
	}
}