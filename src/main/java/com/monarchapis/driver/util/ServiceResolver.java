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

package com.monarchapis.driver.util;

import java.util.List;

import com.google.common.base.Optional;
import com.monarchapis.driver.authentication.ClaimsProcessor;

/**
 * Provides a singleton for looking up beans.
 * 
 * @author Phil Kedy
 */
public abstract class ServiceResolver {
	/**
	 * The service resolver instance.
	 */
	private static ServiceResolver instance;

	/**
	 * Sets the service resolver instance.
	 * 
	 * @param instance
	 *            The service resolver instance.
	 */
	public final static void setInstance(ServiceResolver instance) {
		ServiceResolver.instance = instance;
	}

	/**
	 * Returns the service resolver.
	 * 
	 * @return the service resolver.
	 */
	public final static ServiceResolver getInstance() {
		return instance;
	}

	/**
	 * Returns the bean for a given class.
	 * 
	 * @param clazz
	 *            The class to find.
	 * @return the bean if found, or an exception is thrown otherwise.
	 */
	public abstract <T> T required(Class<T> clazz);

	/**
	 * Returns the bean for a given class.
	 * 
	 * @param clazz
	 *            The class to find.
	 * @return the present bean if found, absent otherwise.
	 */
	public abstract <T> Optional<T> optional(Class<T> clazz);

	public abstract <T> List<T> getInstancesOf(Class<T> clazz);
}
