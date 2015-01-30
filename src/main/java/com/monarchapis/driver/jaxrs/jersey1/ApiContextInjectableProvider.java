package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.model.ApiContext;
import com.sun.jersey.api.core.HttpContext;

@Provider
public class ApiContextInjectableProvider extends AbstractInjectableProvider<ApiContext> {
	public ApiContextInjectableProvider() {
		super(ApiContext.class);
	}

	@Override
	public ApiContext getValue(HttpContext httpContext) {
		return ApiContext.getCurrent();
	}
}