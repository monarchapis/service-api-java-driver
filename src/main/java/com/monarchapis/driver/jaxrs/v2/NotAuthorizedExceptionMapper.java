package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;

@Provider
public class NotAuthorizedExceptionMapper extends AbstractExceptionMapper<NotAuthorizedException> {
	@Override
	protected String getReason() {
		return "notAuthorized";
	}
}