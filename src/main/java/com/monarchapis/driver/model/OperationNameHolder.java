package com.monarchapis.driver.model;

public abstract class OperationNameHolder {
	private static InheritableThreadLocal<String> current = new InheritableThreadLocal<String>();

	public static String getCurrent() {
		return current.get();
	}

	public static void setCurrent(String reason) {
		if (reason != null)
			current.set(reason);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
