package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper extends AbstractExceptionMapper<IllegalArgumentException> {
	@Override
	protected String getReason() {
		return "invalidParameter";
	}
	
	@Override
	protected Object[] getArguments(IllegalArgumentException exception) {
		return new Object[] { exception.getMessage() };
	}
}