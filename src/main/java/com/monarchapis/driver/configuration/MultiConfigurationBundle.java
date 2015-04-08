package com.monarchapis.driver.configuration;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Optional;

public class MultiConfigurationBundle implements ConfigurationBundle {
	private ConfigurationBundle[] bundles;

	public MultiConfigurationBundle() {
		setBundles(new ConfigurationBundle[0]);
	}

	public MultiConfigurationBundle(ConfigurationBundle... bundles) {
		setBundles(bundles);
	}

	public ConfigurationBundle[] getBundles() {
		return bundles;
	}

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
