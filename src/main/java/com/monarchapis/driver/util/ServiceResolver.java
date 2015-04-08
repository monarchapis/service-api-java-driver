package com.monarchapis.driver.util;

import com.google.common.base.Optional;

public abstract class ServiceResolver {
	private static ServiceResolver instance;

	public final static void setInstance(ServiceResolver instance) {
		ServiceResolver.instance = instance;
	}

	public final static ServiceResolver getInstance() {
		return instance;
	}

	public abstract <T> T getService(Class<T> clazz);

	public abstract <T> Optional<T> getOptional(Class<T> clazz);
}
