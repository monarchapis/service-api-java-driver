package com.monarchapis.driver.model;

public abstract class BypassAnalyticsHolder {
	private static InheritableThreadLocal<Boolean> current = new InheritableThreadLocal<Boolean>();

	public static Boolean getCurrent() {
		return current.get();
	}

	public static void setCurrent(Boolean value) {
		if (value != null)
			current.set(value);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
