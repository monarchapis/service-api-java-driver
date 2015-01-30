package com.monarchapis.driver.authentication;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProviderCredentials {
	private String apiKey;
	private String sharedSecret;

	public ProviderCredentials(String apiKey, String shareSecret) {
		if (apiKey == null) {
			throw new IllegalArgumentException("apiKey is required.");
		}

		this.apiKey = apiKey;
		this.sharedSecret = shareSecret;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSharedSecret() {
		return sharedSecret;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
