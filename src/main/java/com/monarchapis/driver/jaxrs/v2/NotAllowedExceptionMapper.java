package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;

@Provider
public class NotAllowedExceptionMapper extends AbstractExceptionMapper<NotAllowedException> {
	@Override
	protected String getReason() {
		return "methodNotAllowed";
	}
}