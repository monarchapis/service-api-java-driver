package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;

@Provider
public class NotFoundExceptionMapper extends AbstractExceptionMapper<NotFoundException> {
	@Override
	protected String getReason() {
		return "notFound";
	}
}