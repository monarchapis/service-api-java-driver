package com.monarchapis.driver.util;

import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.PrincipalContext;

public abstract class ApiContextUtils {
	public static String getUserId() {
		return getUserId(null);
	}

	public static String getUserId(String defaultUserId) {
		PrincipalContext principal = getPrincipal();
		String userId = defaultUserId;

		if (principal != null) {
			userId = principal.getId();
		}

		return userId;
	}

	public static boolean hasClaim(String type) {
		PrincipalContext principal = getPrincipal();
		boolean exists = false;

		if (principal != null) {
			exists = principal.hasClaim(type);
		}

		return exists;
	}

	public static boolean hasClaim(String type, String value) {
		PrincipalContext principal = getPrincipal();
		boolean exists = false;

		if (principal != null) {
			exists = principal.hasClaim(type, value);
		}

		return exists;
	}

	public static PrincipalContext getPrincipal() {
		ApiContext context = ApiContext.getCurrent();
		PrincipalContext principal = null;

		if (context != null) {
			principal = context.getPrincipal();
		}

		return principal;
	}
}
