package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAuthorizedExceptionMapper extends AbstractClientExceptionMapper<NotAuthorizedException> {
	@Override
	public String getReason() {
		return "notAuthorized";
	}

	@Override
	public String getDeveloperMessage() {
		return "Your request failed authorization.";
	}

	@Override
	public String getErrorCode() {
		return "CLIENT-0004";
	}
}