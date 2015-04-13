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

package com.monarchapis.driver.jaxrs.v2;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.monarchapis.driver.model.VersionHolder;

/**
 * Sets the version.
 * 
 * @author Phil Kedy
 */
@Priority(Priorities.AUTHENTICATION)
public class VersionRequestFilter implements ContainerRequestFilter {
	private String version;

	public VersionRequestFilter(String version) {
		this.version = version;
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		VersionHolder.setCurrent(version);
	}

	public String getVersion() {
		return version;
	}
}