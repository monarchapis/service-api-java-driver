/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monarchapis.driver.service.v1;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.api.v1.client.OpenApi;
import com.monarchapis.api.v1.client.OpenResource;
import com.monarchapis.api.v1.model.Reference;
import com.monarchapis.api.v1.model.ServiceInfo;

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
			OpenResource open = openApi.getOpenResource();
			serviceInfo = open.getServiceInfo(environmentName, serviceName, providerKey);

			logger.info("Service initialized: environment: {} / service: {} / provider: {}",
					referenceKVP(serviceInfo.getEnvironment()), //
					referenceKVP(serviceInfo.getService().orNull()), //
					referenceKVP(serviceInfo.getProvider().orNull()));
		}

		return serviceInfo;
	}

	private String referenceKVP(Reference reference) {
		if (reference == null) {
			return "null";
		}

		return reference.getName() + " = " + reference.getId();
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
