package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.util.ErrorUtils;

@Provider
public class ApiResponseExceptionMapper implements ExceptionMapper<ApiErrorException> {
	@Override
	public Response toResponse(ApiErrorException e) {
		return Response.status(e.getCode()).entity(e.getError()).type(ErrorUtils.getBestMediaType()).build();
	}
}