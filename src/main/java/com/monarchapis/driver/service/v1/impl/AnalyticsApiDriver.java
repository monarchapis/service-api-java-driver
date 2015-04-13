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

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monarchapis.driver.service.v1.AnalyticsApi;

public class AnalyticsApiDriver extends AbstractEnvironmentDriver implements AnalyticsApi {
	private static final Logger logger = LoggerFactory.getLogger(AnalyticsApiDriver.class);

	@Override
	public void event(String eventType, ObjectNode data) {
		long begin = System.currentTimeMillis();

		try {
			executeJsonRequest(POST, "/analytics/v1/" + URLEncoder.encode(eventType, "UTF-8") + "/events", data);
		} catch (Exception e) {
			throw new RuntimeException("Could not send event", e);
		} finally {
			long duration = System.currentTimeMillis() - begin;
			logger.debug("Send result took {}ms", duration);
		}
	}
}
