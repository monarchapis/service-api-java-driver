package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.service.v1.EventsApi;
import com.monarchapis.driver.service.v1.ServiceContainer;
import com.sun.jersey.api.core.HttpContext;

@Provider
public class EventsApiInjectableProvider extends AbstractInjectableProvider<EventsApi> {
	public EventsApiInjectableProvider() {
		super(EventsApi.class);
	}

	@Override
	public EventsApi getValue(HttpContext httpContext) {
		return ServiceContainer.getInstance().getEventsApi();
	}
}