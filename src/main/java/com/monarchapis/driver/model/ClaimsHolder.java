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
 * Stores the current {@link Claims} in a thread local.
 * 
 * @author Phil Kedy
 */
public abstract class ClaimsHolder {
	/**
	 * The current API error instance, if an error was thrown.
	 */
	private static InheritableThreadLocal<Claims> current = new InheritableThreadLocal<Claims>();

	private static boolean autoCreate = true;

	public static Claims getCurrent() {
		Claims claims = current.get();

		if (claims == null && autoCreate) {
			claims = new Claims();
			current.set(claims);
		}

		return claims;
	}

	public static void setCurrent(Claims error) {
		if (error != null) {
			current.set(error);
		} else {
			current.remove();
		}
	}

	public static void remove() {
		current.remove();
	}

	public static void setAutoCreate(boolean value) {
		autoCreate = value;
	}
}
