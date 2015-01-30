package com.monarchapis.driver.service.v1;

import com.monarchapis.driver.model.ServiceInfo;

public interface ServiceInfoResolver {
	public ServiceInfo getServiceInfo(String path);
}
