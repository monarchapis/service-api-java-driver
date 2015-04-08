package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.ext.Provider;

@Provider
public class UnsupportedOperationExceptionMapper extends AbstractExceptionMapper<UnsupportedOperationException> {
	@Override
	protected String getReason() {
		return "methodNotAllowed";
	}
}