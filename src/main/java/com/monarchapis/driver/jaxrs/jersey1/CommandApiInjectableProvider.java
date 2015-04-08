package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.service.v1.CommandApi;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.api.core.HttpContext;

@Provider
public class CommandApiInjectableProvider extends AbstractInjectableProvider<CommandApi> {
	public CommandApiInjectableProvider() {
		super(CommandApi.class);
	}

	@Override
	public CommandApi getValue(HttpContext httpContext) {
		return ServiceResolver.getInstance().getService(CommandApi.class);
	}
}