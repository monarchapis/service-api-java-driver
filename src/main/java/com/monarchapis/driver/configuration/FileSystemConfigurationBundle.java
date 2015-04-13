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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * A configuration bundle loaded from the file system.
 * 
 * @author Phil Kedy
 */
public class FileSystemConfigurationBundle extends AbstractConfigurationBundle {
	/**
	 * The base directory location to search for configuration files.
	 */
	private File baseDir;

	/**
	 * The file name prefix to load.
	 */
	private String prefix;

	public FileSystemConfigurationBundle() {
	}

	public FileSystemConfigurationBundle(File baseDir, String prefix) {
		setBaseDir(baseDir);
		setPrefix(prefix);
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		Validate.notNull(baseDir, "baseDir is a required parameter.");
		Validate.isTrue(baseDir.isDirectory(), "The baseDir file object must be a directory.");

		this.baseDir = baseDir;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		Validate.notNull(prefix, "String prefix is a required parameter.");

		this.prefix = prefix;
	}

	@Override
	protected Configuration[] loadConfigurationsVariant(String variant) throws IOException {
		if (baseDir == null || prefix == null) {
			return null;
		}

		String v = StringUtils.isNotBlank(variant) ? "_" + variant : "";

		for (String extension : EXTENSIONS) {
			String filename = prefix + v + '.' + extension;
			File file = new File(baseDir, filename);

			if (!file.exists()) {
				continue;
			}

			LoadableConfiguration configuration = newConfiguration(extension);
			configuration.load(file);

			return new Configuration[] { configuration };
		}

		return null;
	}
}
