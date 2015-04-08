package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapper;

@Provider
public class NotSupportedExceptionMapper extends AbstractExceptionMapper<NotSupportedException> {
	@Override
	public String getReason() {
		return "mediaTypeNotSupported";
	}
}