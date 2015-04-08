package com.monarchapis.driver.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class FileSystemConfigurationBundle extends AbstractConfigurationBundle {
	private File baseDir;

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
