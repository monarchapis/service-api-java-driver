package com.monarchapis.driver.service.v1.mock;

import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public class MockServiceInfoResolver implements ServiceInfoResolver {
	private ServiceInfo serviceInfo;

	public MockServiceInfoResolver(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	@Override
	public ServiceInfo getServiceInfo(String path) {
		return serviceInfo;
	}
}
