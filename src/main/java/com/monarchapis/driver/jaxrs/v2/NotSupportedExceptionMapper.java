package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotSupportedExceptionMapper extends AbstractClientExceptionMapper<NotSupportedException> {
	@Override
	public String getReason() {
		return "unsupportedMediaType";
	}

	@Override
	public String getDeveloperMessage() {
		return "Your request entity media type is unsupported for this operation.";
	}

	@Override
	public String getErrorCode() {
		return "CLIENT-0002";
	}
}