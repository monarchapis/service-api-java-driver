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
 * Stores the flag to bypass sending traffic statistics to Monarch in a thread
 * local.
 * 
 * @author Phil Kedy
 */
public abstract class BypassAnalyticsHolder {
	/**
	 * The current flag value for the thread.
	 */
	private static InheritableThreadLocal<Boolean> current = new InheritableThreadLocal<Boolean>();

	public static Boolean getCurrent() {
		return current.get();
	}

	public static void setCurrent(Boolean value) {
		if (value != null)
			current.set(value);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
