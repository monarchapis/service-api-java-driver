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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.monarchapis.driver.exception.ApiException;

/**
 * Provides general functionality for returning configuration properties from
 * configuration bundles.
 * 
 * @author Phil Kedy
 */
public abstract class AbstractConfigurationBundle implements ConfigurationBundle {
	/**
	 * A value used to cache variants that could not be found.
	 */
	private static final Configuration[] EMPTY_CONFIGURATIONS = new Configuration[0];

	/**
	 * The supported file extensions.
	 */
	protected static final String[] EXTENSIONS = new String[] { "yaml", "json" };

	/**
	 * The extension to configuration implementation map.
	 */
	protected static final Map<String, Class<? extends LoadableConfiguration>> EXTENSIONS_MAP;

	static {
		Map<String, Class<? extends LoadableConfiguration>> map = new HashMap<String, Class<? extends LoadableConfiguration>>();
		map.put("json", JsonConfiguration.class);
		map.put("yaml", YamlConfiguration.class);

		EXTENSIONS_MAP = Collections.unmodifiableMap(map);
	}

	/**
	 * The cached variants.
	 */
	private Map<String, Configuration[]> cachedVariants = new HashMap<String, Configuration[]>();

	/**
	 * Instantiates a new loadable configuration object based on the extension.
	 * 
	 * @param extension
	 *            The configuration file extension
	 * @return the loadable configuration object.
	 */
	protected LoadableConfiguration newConfiguration(String extension) {
		try {
			return EXTENSIONS_MAP.get(extension.toLowerCase()).newInstance();
		} catch (Exception e) {
			throw new ApiException("Could not find configuration class for extension " + extension);
		}
	}

	@Override
	public boolean hasValue(String path, String[] variants) {
		for (String variant : variants) {
			Configuration configs[] = getConfigurationsForVariant(variant);

			if (configs != null && configs.length > 0) {
				for (int i = configs.length - 1; i >= 0; i--) {
					if (configs[i].hasValue(path)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public Optional<String> getString(String path, String[] variants, Object... args) {
		for (String variant : variants) {
			Optional<String> value = getVariantString(path, variant, args);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantString(path, "", args);
	}

	/**
	 * Returns a string property for a single variant.
	 * 
	 * @param path
	 *            The property path
	 * @param variant
	 *            The variant name
	 * @param args
	 *            The optional list of arguments for message formatting
	 * @return a present string of the property was found, an absent string
	 *         otherwise.
	 */
	protected Optional<String> getVariantString(String path, String variant, Object... args) {
		Configuration configs[] = getConfigurationsForVariant(variant);

		if (configs != null && configs.length > 0) {
			for (int i = configs.length - 1; i >= 0; i--) {
				Optional<String> value = configs[i].getString(path);

				if (value.isPresent()) {
					return Optional.of(MessageFormat.format(value.get(), args));
				}
			}
		}

		return Optional.absent();
	}

	@Override
	public Optional<Integer> getInteger(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Integer> value = getVariantInteger(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantInteger(path, "");
	}

	/**
	 * Returns an integer property for a single variant.
	 * 
	 * @param path
	 *            The property path
	 * @param variant
	 *            The variant name
	 * @return a present integer if found, an absent integer otherwise.
	 */
	protected Optional<Integer> getVariantInteger(String path, String variant) {
		Configuration configs[] = getConfigurationsForVariant(variant);

		if (configs != null && configs.length > 0) {
			for (int i = configs.length - 1; i >= 0; i--) {
				Optional<Integer> value = configs[i].getInteger(path);

				if (value.isPresent()) {
					return value;
				}
			}
		}

		return Optional.absent();
	}

	@Override
	public Optional<Long> getLong(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Long> value = getVariantLong(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantLong(path, "");
	}

	/**
	 * Returns a long property for a single variant.
	 * 
	 * @param path
	 *            The property path
	 * @param variant
	 *            The variant name
	 * @return a present long if found, an absent long otherwise.
	 */
	protected Optional<Long> getVariantLong(String path, String variant) {
		Configuration configs[] = getConfigurationsForVariant(variant);

		if (configs != null && configs.length > 0) {
			for (int i = configs.length - 1; i >= 0; i--) {
				Optional<Long> value = configs[i].getLong(path);

				if (value.isPresent()) {
					return value;
				}
			}
		}

		return Optional.absent();
	}

	@Override
	public Optional<Boolean> getBoolean(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Boolean> value = getVariantBoolean(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantBoolean(path, "");
	}

	/**
	 * Returns a boolean property for a single variant.
	 * 
	 * @param path
	 *            The property path
	 * @param variant
	 *            The variant name
	 * @return a present boolean if found, an absent boolean otherwise.
	 */
	protected Optional<Boolean> getVariantBoolean(String path, String variant) {
		Configuration configs[] = getConfigurationsForVariant(variant);

		if (configs != null && configs.length > 0) {
			for (int i = configs.length - 1; i >= 0; i--) {
				Optional<Boolean> value = configs[i].getBoolean(path);

				if (value.isPresent()) {
					return value;
				}
			}
		}

		return Optional.absent();
	}

	@Override
	public Optional<Double> getDouble(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Double> value = getVariantDouble(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantDouble(path, "");
	}

	/**
	 * Returns a double property for a single variant.
	 * 
	 * @param path
	 *            The property path
	 * @param variant
	 *            The variant name
	 * @return a present double if found, an absent double otherwise.
	 */
	protected Optional<Double> getVariantDouble(String path, String variant) {
		Configuration configs[] = getConfigurationsForVariant(variant);

		if (configs != null && configs.length > 0) {
			for (int i = configs.length - 1; i >= 0; i--) {
				Optional<Double> value = configs[i].getDouble(path);

				if (value.isPresent()) {
					return value;
				}
			}
		}

		return Optional.absent();
	}

	/**
	 * Attempts to retrieve the configurations for a variant from cache. If not
	 * found in cache, it will attempt to load it. If the configuration is not
	 * found, empty configurations are added to the cache to prevent future
	 * cycles.
	 * 
	 * @param variant
	 *            The variant name
	 * @return the array of configurations for the variant.
	 */
	private Configuration[] getConfigurationsForVariant(String variant) {
		Configuration[] configs = cachedVariants.get(variant);

		if (configs == null) {
			try {
				configs = loadConfigurationsVariant(variant);

				if (configs == null) {
					configs = EMPTY_CONFIGURATIONS;
				}

				cachedVariants.put(variant, configs);
			} catch (IOException e) {
				throw new ApiException("Could not load configuration for variant " + variant, e);
			}
		}

		return configs;
	}

	/**
	 * Loads configurations for a given variant.
	 * 
	 * @param variant
	 *            The variant name.
	 * @return the array of configurations for the given variant.
	 * @throws IOException
	 *             when there was a problem reading a configuration file.
	 */
	protected abstract Configuration[] loadConfigurationsVariant(String variant) throws IOException;
}
