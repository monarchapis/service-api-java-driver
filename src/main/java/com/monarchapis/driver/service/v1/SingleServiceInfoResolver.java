package com.monarchapis.driver.service.v1;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.model.ServiceInfo;

public class SingleServiceInfoResolver implements ServiceInfoResolver {
	private static Logger logger = LoggerFactory.getLogger(SingleServiceInfoResolver.class);

	private OpenApi openApi;
	private String environmentName;
	private String serviceName;
	private String providerKey;

	private ServiceInfo serviceInfo;

	public SingleServiceInfoResolver(OpenApi openApi, String environmentName, String serviceName, String providerKey) {
		this.openApi = openApi;
		this.environmentName = StringUtils.trimToNull(environmentName);
		this.serviceName = StringUtils.trimToNull(serviceName);
		this.providerKey = StringUtils.trimToNull(providerKey);
	}

	@Override
	public ServiceInfo getServiceInfo(String path) {
		if (serviceInfo == null) {
			logger.info("Looking up Service Information for {}/{}...", environmentName, serviceName);
			serviceInfo = openApi.getServiceInfo(environmentName, serviceName, providerKey);
			logger.info("Service initialized: {}", serviceInfo);
		}

		return serviceInfo;
	}
}
