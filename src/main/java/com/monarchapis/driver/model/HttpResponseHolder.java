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

import javax.servlet.http.HttpServletResponse;

/**
 * Stores the current HTTP response object in a thread local.
 * 
 * @author Phil Kedy
 */
public abstract class HttpResponseHolder {
	/**
	 * The current <code>HttpServletResponse</code> instance for the thread.
	 */
	private static InheritableThreadLocal<HttpServletResponse> current = new InheritableThreadLocal<HttpServletResponse>();

	public static HttpServletResponse getCurrent() {
		return current.get();
	}

	public static void setCurrent(HttpServletResponse response) {
		if (response != null)
			current.set(response);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
