package com.monarchapis.driver.configuration;

import com.google.common.base.Optional;

public interface ConfigurationBundle {
	public Optional<String> getString(String path, String[] variants, Object... args);

	public Optional<Integer> getInteger(String path, String[] variants);

	public Optional<Long> getLong(String path, String[] variants);

	public Optional<Boolean> getBoolean(String path, String[] variants);

	public Optional<Double> getDouble(String path, String[] variants);
}
