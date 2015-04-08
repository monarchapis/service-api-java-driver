package com.monarchapis.driver.service.v1;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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

	public SingleServiceInfoResolver() {
	}

	public SingleServiceInfoResolver(OpenApi openApi, String environmentName, String serviceName, String providerKey) {
		setOpenApi(openApi);
		setEnvironmentName(environmentName);
		setServiceName(serviceName);
		setProviderKey(providerKey);
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

	public void setOpenApi(OpenApi openApi) {
		Validate.notNull(openApi, "openApi is required");
		this.openApi = openApi;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = StringUtils.trimToNull(environmentName);
	}

	public void setServiceName(String serviceName) {
		this.serviceName = StringUtils.trimToNull(serviceName);
	}

	public void setProviderKey(String providerKey) {
		this.providerKey = StringUtils.trimToNull(providerKey);
	}
}
