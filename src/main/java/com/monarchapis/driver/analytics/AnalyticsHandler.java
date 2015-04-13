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

package com.monarchapis.driver.analytics;

import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

/**
 * Handles collecting analytics events into a data store.
 * 
 * @author Phil Kedy
 */
public interface AnalyticsHandler {
	/**
	 * Collect the event data.
	 * 
	 * @param request
	 *            The API request
	 * @param response
	 *            The API response
	 * @param ms
	 *            The time in milliseconds the request took to process
	 */
	void collect(ApiRequest request, ApiResponse response, long ms);
}
