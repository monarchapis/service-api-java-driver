package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.model.OperationNameHolder;

@Priority(Priorities.AUTHENTICATION)
public class OperationNameRequestFilter implements ContainerRequestFilter {
	private String operationName;

	public OperationNameRequestFilter(String operationName) {
		this.operationName = operationName;
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		OperationNameHolder.setCurrent(operationName);
	}
}
