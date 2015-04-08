package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;
import com.sun.jersey.api.NotFoundException;

@Provider
public class NotFoundExceptionMapper extends AbstractExceptionMapper<NotFoundException> {
	@Override
	public String getReason() {
		return "notFound";
	}
}