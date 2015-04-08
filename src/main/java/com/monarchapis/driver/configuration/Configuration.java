package com.monarchapis.driver.configuration;

import com.google.common.base.Optional;

public interface Configuration {
	public Optional<String> getString(String path, Object... args);

	public Optional<Integer> getInteger(String path);

	public Optional<Long> getLong(String path);

	public Optional<Boolean> getBoolean(String path);

	public Optional<Double> getDouble(String path);
}
