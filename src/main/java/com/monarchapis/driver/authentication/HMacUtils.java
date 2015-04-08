package com.monarchapis.driver.authentication;

import java.util.HashMap;
import java.util.Map;

import com.monarchapis.driver.exception.ApiException;

public final class HmacUtils {
	private static Map<String, String> algrithmLookup = new HashMap<String, String>();

	static {
		algrithmLookup.put("md5", "HmacMD5");
		algrithmLookup.put("sha1", "HmacSHA1");
		algrithmLookup.put("sha256", "HmacSHA256");
	}

	private HmacUtils() {
	}

	public static String getHMacAlgorithm(String algorithm) {
		String digestAlgorithm = algrithmLookup.get(algorithm);

		if (digestAlgorithm == null) {
			throw new ApiException("Invalid algorithm " + algorithm);
		}

		return digestAlgorithm;
	}
}
