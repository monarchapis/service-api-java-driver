package com.monarchapis.driver.service.v1;

import com.monarchapis.driver.model.ServiceInfo;

public interface OpenApi {
	public ServiceInfo getServiceInfo(String environmentName, String serviceName, String providerKey);
}
