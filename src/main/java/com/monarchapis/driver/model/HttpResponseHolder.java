package com.monarchapis.driver.model;

import javax.servlet.http.HttpServletResponse;

public abstract class HttpResponseHolder {
	private static InheritableThreadLocal<HttpServletResponse> current = new InheritableThreadLocal<HttpServletResponse>();

	public static HttpServletResponse getCurrent() {
		return current.get();
	}

	public static void setCurrent(HttpServletResponse response) {
		if (response != null)
			current.set(response);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}
}
