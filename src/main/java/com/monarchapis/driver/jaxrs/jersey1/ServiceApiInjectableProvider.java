package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.api.core.HttpContext;

@Provider
public class ServiceApiInjectableProvider extends AbstractInjectableProvider<ServiceApi> {
	public ServiceApiInjectableProvider() {
		super(ServiceApi.class);
	}

	@Override
	public ServiceApi getValue(HttpContext httpContext) {
		return ServiceResolver.getInstance().getService(ServiceApi.class);
	}
}