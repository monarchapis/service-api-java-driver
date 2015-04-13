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

import org.apache.commons.lang3.Validate;

import com.google.common.base.Optional;

/**
 * Searches for property values in multiple configurations. The search starts
 * from the last configuration and works backwards until it either finds a value
 * or has exhausted the configurations.
 * 
 * @author Phil Kedy
 */
public class MultiConfigurationBundle implements ConfigurationBundle {
	/**
	 * A single reference of an empty array for the default constructor to use.
	 */
	private static ConfigurationBundle[] EMPTY_BUNDLES = new ConfigurationBundle[0];

	/**
	 * The instance configuration bundles.
	 */
	private ConfigurationBundle[] bundles;

	/**
	 * Constructs the multi bundle with no bundles assuming the setBundles
	 * method will be called to set the desired bundles.
	 */
	public MultiConfigurationBundle() {
		setBundles(EMPTY_BUNDLES);
	}

	/**
	 * Constructs the multi bundle with the desired bundles.
	 * 
	 * @param bundles
	 *            The configuration bundles
	 */
	public MultiConfigurationBundle(ConfigurationBundle... bundles) {
		setBundles(bundles);
	}

	/**
	 * Returns the current bundles.
	 * 
	 * @return the current bundles.
	 */
	public ConfigurationBundle[] getBundles() {
		return bundles;
	}

	/**
	 * Sets the bundles to search through.
	 * 
	 * @param bundles
	 *            The array of bundles.
	 */
	public void setBundles(ConfigurationBundle[] bundles) {
		Validate.notNull(bundles, "bundles is a required parameter.");
		this.bundles = bundles;
	}

	@Override
	public Optional<String> getString(String path, String[] variants, Object... args) {
		Optional<String> value = Optional.absent();

		for (int i = bundles.length - 1; i >= 0; i--) {
			value = bundles[i].getString(path, variants, args);

			if (value.isPresent()) {
				break;
			}
		}

		return value;
	}

	@Override
	public Optional<Integer> getInteger(String path, String[] variants) {
		Optional<Integer> value = Optional.absent();

		for (int i = bundles.length - 1; i >= 0; i--) {
			value = bundles[i].getInteger(path, variants);

			if (value.isPresent()) {
				break;
			}
		}

		return value;
	}

	@Override
	public Optional<Long> getLong(String path, String[] variants) {
		Optional<Long> value = Optional.absent();

		for (int i = bundles.length - 1; i >= 0; i--) {
			value = bundles[i].getLong(path, variants);

			if (value.isPresent()) {
				break;
			}
		}

		return value;
	}

	@Override
	public Optional<Double> getDouble(String path, String[] variants) {
		Optional<Double> value = Optional.absent();

		for (int i = bundles.length - 1; i >= 0; i--) {
			value = bundles[i].getDouble(path, variants);

			if (value.isPresent()) {
				break;
			}
		}

		return value;
	}

	@Override
	public Optional<Boolean> getBoolean(String path, String[] variants) {
		Optional<Boolean> value = Optional.absent();

		for (int i = bundles.length - 1; i >= 0; i--) {
			value = bundles[i].getBoolean(path, variants);

			if (value.isPresent()) {
				break;
			}
		}

		return value;
	}
}
