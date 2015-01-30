package com.monarchapis.driver.hash;

import java.util.HashMap;
import java.util.Map;

import com.monarchapis.driver.exception.ApiException;

public final class HasherUtils {
	private static Map<String, String> algrithmLookup = new HashMap<String, String>();

	static {
		algrithmLookup.put("md5", "MD5");
		algrithmLookup.put("sha1", "SHA-1");
		algrithmLookup.put("sha256", "SHA-256");
		algrithmLookup.put("sha384", "SHA-384");
		algrithmLookup.put("sha512", "SHA-512");
	}

	private HasherUtils() {
	}

	public static String getMessageDigestAlgorithm(String algorithm) {
		String digestAlgorithm = algrithmLookup.get(algorithm);

		if (digestAlgorithm == null) {
			throw new ApiException("Invalid algorithm " + algorithm);
		}

		return digestAlgorithm;
	}
}
