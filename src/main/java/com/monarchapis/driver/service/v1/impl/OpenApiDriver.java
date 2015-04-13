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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.model.ServiceInfo;
import com.monarchapis.driver.service.v1.OpenApi;

public class OpenApiDriver extends AbstractDriver implements OpenApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OpenApiDriver.class);

	public ServiceInfo getServiceInfo(String environmentName, String serviceName, String providerKey) {
		try {
			StringBuilder sb = new StringBuilder("/open/v1/serviceInfo") //
					.append("?environmentName=") //
					.append(encodeUriComponent(environmentName));

			if (serviceName != null) {
				sb.append("&serviceName=") //
						.append(encodeUriComponent(serviceName));
			}

			if (providerKey != null) {
				sb.append("&providerKey=") //
						.append(encodeUriComponent(providerKey));
			}

			GenericUrl genericUrl = createUrl(sb.toString());
			HttpRequest request = getRequestFactory().buildGetRequest(genericUrl);
			HttpHeaders headers = request.getHeaders();
			headers.setAccept("application/json");
			headers.setContentEncoding("UTF-8");

			HttpResponse response = request.execute();
			ServiceInfo serviceInfo = parseAs(response, ServiceInfo.class);

			return serviceInfo;
		} catch (Exception e) {
			throw new ApiException("Error resolving service information", e);
		}
	}
}
