package com.monarchapis.driver.model;

public abstract class VersionHolder {
	private static InheritableThreadLocal<String> current = new InheritableThreadLocal<String>();

	public static String getCurrent() {
		return current.get();
	}

	public static void setCurrent(String value) {
		if (value != null) {
			current.set(value);
		} else {
			current.remove();
		}
	}

	public static void remove() {
		current.remove();
	}
}
