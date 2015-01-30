package com.monarchapis.driver.model;

import com.monarchapis.driver.exception.ApiError;

public abstract class ErrorHolder {
	private static InheritableThreadLocal<ApiError> current = new InheritableThreadLocal<ApiError>();

	public static ApiError getCurrent() {
		return current.get();
	}

	public static void setCurrent(ApiError error) {
		if (error != null)
			current.set(error);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
