package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper extends AbstractClientExceptionMapper<BadRequestException> {
	@Override
	public String getReason() {
		return "badRequest";
	}

	@Override
	public String getDeveloperMessage() {
		return "This was a bad request.";
	}

	@Override
	public String getErrorCode() {
		return "CLIENT-0004";
	}
}