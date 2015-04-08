package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.model.BypassAnalyticsHolder;

@Priority(Priorities.AUTHENTICATION)
public class BypassAnalyticsRequestFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		BypassAnalyticsHolder.setCurrent(true);
	}
}
