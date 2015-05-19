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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * A configuration bundle loaded from the file system.
 * 
 * @author Phil Kedy
 */
public class FileSystemConfigurationBundle extends AbstractConfigurationBundle {
	/**
	 * A single reference of an empty array for the default constructor to use.
	 */
	private static String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * The base directory location to search for configuration files.
	 */
	private File baseDir;

	/**
	 * The file name prefix to load.
	 */
	private String[] prefixes;

	public FileSystemConfigurationBundle() {
		setBaseDir(new File(System.getProperty("user.dir")));
		setPrefixes(EMPTY_STRING_ARRAY);
	}

	public FileSystemConfigurationBundle(File baseDir, String... prefixes) {
		setBaseDir(baseDir);
		setPrefixes(prefixes);
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		Validate.notNull(baseDir, "baseDir is a required parameter.");
		Validate.isTrue(baseDir.isDirectory(), "The baseDir file object must be a directory.");

		this.baseDir = baseDir;
	}

	public String[] getPrefixes() {
		return prefixes;
	}

	public void setPrefix(String prefix) {
		Validate.notNull(prefix, "prefix is a required parameter.");

		this.prefixes = new String[] { prefix };
	}

	public void setPrefixes(String... prefixes) {
		Validate.notNull(prefixes, "prefixes is a required parameter.");

		this.prefixes = prefixes;
	}

	@Override
	protected Configuration[] loadConfigurationsVariant(String variant) throws IOException {
		String v = StringUtils.isNotBlank(variant) ? "_" + variant : "";
		List<Configuration> configs = new ArrayList<Configuration>();

		for (String prefix : prefixes) {
			for (String extension : EXTENSIONS) {
				String filename = prefix + v + '.' + extension;
				File file = new File(baseDir, filename);

				if (!file.exists()) {
					continue;
				}

				LoadableConfiguration configuration = newConfiguration(extension);
				configuration.load(file);

				configs.add(configuration);
			}
		}

		return configs.toArray(new Configuration[configs.size()]);
	}
}
