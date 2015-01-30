package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedExceptionMapper extends AbstractClientExceptionMapper<NotAllowedException> {
	@Override
	public String getReason() {
		return "notAllowed";
	}

	@Override
	public String getDeveloperMessage() {
		return "Your requesting a resource method that is not allowed.";
	}

	@Override
	public String getErrorCode() {
		return "CLIENT-0003";
	}
}