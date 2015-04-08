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

public class ResourceConfigurationBundle extends AbstractConfigurationBundle {
	private static final Logger logger = LoggerFactory.getLogger(ResourceConfigurationBundle.class);

	private static String[] EMPTY_STRING_ARRAY = new String[0];

	private ClassLoader bundleClassLoader;

	private String[] basenames;

	public ResourceConfigurationBundle() {
		this(ResourceConfigurationBundle.class.getClassLoader(), EMPTY_STRING_ARRAY);
	}

	public ResourceConfigurationBundle(ClassLoader bundleClassLoader) {
		this(bundleClassLoader, EMPTY_STRING_ARRAY);
	}

	public ResourceConfigurationBundle(String... basenames) {
		this(ResourceConfigurationBundle.class.getClassLoader(), basenames);
	}

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

		for (int i = basenames.length - 1; i >= 0; i--) {
			String basename = basenames[i];

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
