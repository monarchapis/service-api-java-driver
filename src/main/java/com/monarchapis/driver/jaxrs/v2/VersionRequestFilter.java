package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.model.VersionHolder;

@Priority(Priorities.AUTHENTICATION)
public class VersionRequestFilter implements ContainerRequestFilter {
	private String version;

	public VersionRequestFilter(String version) {
		this.version = version;
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		VersionHolder.setCurrent(version);
	}
}
