package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends AbstractClientExceptionMapper<NotFoundException> {
	@Override
	public String getReason() {
		return "notFound";
	}

	@Override
	public String getDeveloperMessage() {
		return "The resource you requested was not found.";
	}

	@Override
	public String getErrorCode() {
		return "CLIENT-0001";
	}
}