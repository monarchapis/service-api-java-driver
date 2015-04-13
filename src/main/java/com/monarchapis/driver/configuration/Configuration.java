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

package com.monarchapis.driver.configuration;

import com.google.common.base.Optional;

/**
 * Defines the basic interface for retrieving property values from a
 * configuration file.
 * 
 * @author Phil Kedy
 */
public interface Configuration {
	/**
	 * Retrieves the property value as a string.
	 * 
	 * @param path
	 *            The property path
	 * @param args
	 *            The arguments for message formatting
	 * @return the present and formatted string value if found, an absent string
	 *         otherwise.
	 */
	public Optional<String> getString(String path, Object... args);

	/**
	 * Retrieves the property value as an integer.
	 * 
	 * @param path
	 *            The property path
	 * @return the present integer value if found, an absent integer otherwise.
	 */
	public Optional<Integer> getInteger(String path);

	/**
	 * Retrieves the property value as a long.
	 * 
	 * @param path
	 *            The property path
	 * @return the present long value if found, an absent long otherwise.
	 */
	public Optional<Long> getLong(String path);

	/**
	 * Retrieves the property value as a boolean.
	 * 
	 * @param path
	 *            The property path
	 * @return the present boolean value if found, an absent boolean otherwise.
	 */
	public Optional<Boolean> getBoolean(String path);

	/**
	 * Retrieves the property value as a double.
	 * 
	 * @param path
	 *            The property path
	 * @return the present double value if found, an absent double otherwise.
	 */
	public Optional<Double> getDouble(String path);
}
