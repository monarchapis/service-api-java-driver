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

package com.monarchapis.driver.service.v1.impl;

import javax.inject.Inject;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;

public abstract class AbstractEnvironmentDriver extends AbstractDriver {
	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	@Override
	protected void setCustomHeaders(HttpRequest request, HttpHeaders headers) {
		ServiceInfo serviceInfo = serviceInfoResolver.getServiceInfo(request.getUrl().getRawPath());
		headers.set("X-Environment-Id", serviceInfo.getEnvironment().getId());
	}

	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}
}
