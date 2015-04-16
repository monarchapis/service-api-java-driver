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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of a configuration bundle that uses resources as the
 * source.
 * 
 * @author Phil Kedy
 */
public class ResourceConfigurationBundle extends AbstractConfigurationBundle {
	private static final Logger logger = LoggerFactory.getLogger(ResourceConfigurationBundle.class);

	/**
	 * A single reference of an empty array for the default constructor to use.
	 */
	private static String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * The instance class loader used to load resource streams.
	 */
	private ClassLoader bundleClassLoader;

	/**
	 * The array of basenames to open as resources. The variant and extension
	 * are appended dynamically when the variant requested for the first time.
	 */
	private String[] basenames;

	/**
	 * Constructs the bundle using the class loader of this class and no
	 * basenames. Its assumes that setBasename or setBasenames will be called
	 * and setBundleClassLoader may be called after construction.
	 */
	public ResourceConfigurationBundle() {
		this(ResourceConfigurationBundle.class.getClassLoader(), EMPTY_STRING_ARRAY);
	}

	/**
	 * Constructs the bundle with an alternative class loader. Its assumes that
	 * setBasename or setBasenames will be called after construction.
	 * 
	 * @param bundleClassLoader
	 */
	public ResourceConfigurationBundle(ClassLoader bundleClassLoader) {
		this(bundleClassLoader, EMPTY_STRING_ARRAY);
	}

	/**
	 * Constructs the bundle using the class loader of this class and the
	 * desired basenames.
	 * 
	 * @param basenames
	 *            The array of basenames
	 */
	public ResourceConfigurationBundle(String... basenames) {
		this(ResourceConfigurationBundle.class.getClassLoader(), basenames);
	}

	/**
	 * Constructs the bundle using the desired class loader and basenames.
	 * 
	 * @param bundleClassLoader
	 *            The bundle class loader
	 * @param basenames
	 *            The array of basenames
	 */
	public ResourceConfigurationBundle(ClassLoader bundleClassLoader, String... basenames) {
		setBundleClassLoader(bundleClassLoader);
		setBasenames(basenames);
	}

	public ClassLoader getBundleClassLoader() {
		return bundleClassLoader;
	}

	public void setBundleClassLoader(ClassLoader bundleClassLoader) {
		Validate.notNull(bundleClassLoader, "bundleClassLoader is a required parameter.");

		this.bundleClassLoader = bundleClassLoader;
	}

	public String[] getBasenames() {
		return basenames;
	}

	public void setBasename(String basename) {
		Validate.notNull(basename, "basename is a required parameter.");

		this.basenames = new String[] { basename };
	}

	public void setBasenames(String... basenames) {
		Validate.notNull(basenames, "basenames is a required parameter.");

		this.basenames = basenames;
	}

	@Override
	protected Configuration[] loadConfigurationsVariant(String variant) throws IOException {
		String variantPart = StringUtils.isNotBlank(variant) ? "_" + variant : "";
		List<Configuration> configs = new ArrayList<Configuration>();

		for (String basename : basenames) {
			while (basename.startsWith("/")) {
				basename = basename.substring(1);
			}

			for (String extension : EXTENSIONS) {
				String resourceName = basename + variantPart + "." + extension;
				InputStream resource = bundleClassLoader.getResourceAsStream(resourceName);

				if (resource != null) {
					logger.debug("Loading resource for {}", resourceName);
					LoadableConfiguration configuration = newConfiguration(extension);
					configuration.load(resource);
					IOUtils.closeQuietly(resource);

					configs.add(configuration);
				}
			}
		}

		return configs.toArray(new Configuration[configs.size()]);
	}
}
