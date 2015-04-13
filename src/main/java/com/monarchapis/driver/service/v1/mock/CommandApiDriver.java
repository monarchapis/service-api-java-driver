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

package com.monarchapis.driver.service.v1.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.service.v1.CommandApi;

public class CommandApiDriver implements CommandApi {
	private static final Logger logger = LoggerFactory.getLogger(CommandApiDriver.class);

	@Override
	public void sendCommand(String eventType, Object data) {
		logger.info("Event: {} {}", eventType, data);
	}
}