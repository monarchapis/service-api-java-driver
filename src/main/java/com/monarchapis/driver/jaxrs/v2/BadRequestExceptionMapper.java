package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;

@Provider
public class BadRequestExceptionMapper extends AbstractExceptionMapper<BadRequestException> {
	@Override
	protected String getReason() {
		return "badRequest";
	}
}