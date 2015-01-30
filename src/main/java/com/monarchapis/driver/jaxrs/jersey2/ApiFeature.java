package com.monarchapis.driver.jaxrs.jersey2;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiFeature implements Feature {
	@Override
	public boolean configure(final FeatureContext context) {
		context.register(new ApiInjectionBinder());

		return true;
	}
}