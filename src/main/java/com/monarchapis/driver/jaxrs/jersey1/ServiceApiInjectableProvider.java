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

package com.monarchapis.driver.jaxrs.jersey1;

import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.api.core.HttpContext;

/**
 * Provides support for injecting the <code>ServiceApi</code> via the @ApiInject
 * annotation.
 * 
 * @author Phil Kedy
 */
@Provider
public class ServiceApiInjectableProvider extends AbstractInjectableProvider<ServiceApi> {
	public ServiceApiInjectableProvider() {
		super(ServiceApi.class);
	}

	@Override
	public ServiceApi getValue(HttpContext httpContext) {
		return ServiceResolver.getInstance().required(ServiceApi.class);
	}
}