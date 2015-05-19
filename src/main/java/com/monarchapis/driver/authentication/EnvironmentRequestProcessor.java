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

package com.monarchapis.driver.authentication;

import javax.inject.Inject;

import com.monarchapis.api.v1.model.ServiceInfo;
import com.monarchapis.client.rest.BaseClient;
import com.monarchapis.client.rest.RequestProcessor;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public class EnvironmentRequestProcessor implements RequestProcessor {
	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	public EnvironmentRequestProcessor() {
	}

	public EnvironmentRequestProcessor(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}

	public ServiceInfoResolver getServiceInfoResolver() {
		return serviceInfoResolver;
	}

	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}

	@Override
	public void processRequest(BaseClient<?> client) {
		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(client.getPath());

		client.addHeader("X-Environment-Id", serviceInfo.getEnvironment().getId());
	}
}
