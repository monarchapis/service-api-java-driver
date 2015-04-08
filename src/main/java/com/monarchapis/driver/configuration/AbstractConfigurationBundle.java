package com.monarchapis.driver.configuration;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.monarchapis.driver.exception.ApiException;

public abstract class AbstractConfigurationBundle implements ConfigurationBundle {
	private static final Configuration[] EMPTY_CONFIGURATIONS = new Configuration[0];

	protected static final String[] EXTENSIONS = new String[] { "yaml", "json" };

	protected static final Map<String, Class<? extends LoadableConfiguration>> EXTENSIONS_MAP;

	static {
		Map<String, Class<? extends LoadableConfiguration>> map = new HashMap<String, Class<? extends LoadableConfiguration>>();
		map.put("json", JsonConfiguration.class);
		map.put("yaml", YamlConfiguration.class);

		EXTENSIONS_MAP = Collections.unmodifiableMap(map);
	}

	private Map<String, Configuration[]> cachedVariants = new HashMap<String, Configuration[]>();

	protected LoadableConfiguration newConfiguration(String extension) {
		try {
			return EXTENSIONS_MAP.get(extension).newInstance();
		} catch (Exception e) {
			throw new ApiException("Could not find configuration class for extension " + extension);
		}
	}

	public Optional<String> getString(String path, String[] variants, Object... args) {
		for (String variant : variants) {
			Optional<String> value = getVariantString(path, variant, args);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantString(path, "", args);
	}

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

	public Optional<Integer> getInteger(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Integer> value = getVariantInteger(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantInteger(path, "");
	}

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

	public Optional<Long> getLong(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Long> value = getVariantLong(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantLong(path, "");
	}

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

	public Optional<Boolean> getBoolean(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Boolean> value = getVariantBoolean(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantBoolean(path, "");
	}

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

	public Optional<Double> getDouble(String path, String[] variants) {
		for (String variant : variants) {
			Optional<Double> value = getVariantDouble(path, variant);

			if (value.isPresent()) {
				return value;
			}
		}

		return getVariantDouble(path, "");
	}

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

	protected abstract Configuration[] loadConfigurationsVariant(String variant) throws IOException;
}
