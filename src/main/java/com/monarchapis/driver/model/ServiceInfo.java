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

package com.monarchapis.driver.model;

/**
 * Encapsulates references for the environment, service, and provider. These
 * reference fields are sent to the analytics service when logging traffic
 * statistics.
 * 
 * @author Phil Kedy
 */
public class ServiceInfo {
	/**
	 * The environment reference.
	 */
	private Reference environment;

	/**
	 * The service reference.
	 */
	private Reference service;

	/**
	 * The provider reference.
	 */
	private Reference provider;

	public Reference getEnvironment() {
		return environment;
	}

	public void setEnvironment(Reference environment) {
		this.environment = environment;
	}

	public Reference getService() {
		return service;
	}

	public void setService(Reference service) {
		this.service = service;
	}

	public Reference getProvider() {
		return provider;
	}

	public void setProvider(Reference provider) {
		this.provider = provider;
	}

	public String toString() {
		return "environment: " + environment + " / service: " + service + " / provider: " + provider;
	}
}
