package com.monarchapis.driver.model;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProviderCredentials {
	private String apiKey;
	private String sharedSecret;

	public ProviderCredentials() {
	}

	public ProviderCredentials(String apiKeyt) {
		setApiKey(apiKey);
	}

	public ProviderCredentials(String apiKey, String shareSecret) {
		setApiKey(apiKey);
		this.sharedSecret = shareSecret;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		Validate.notBlank(apiKey, "apiKey is required.");
		this.apiKey = apiKey;
	}

	public String getSharedSecret() {
		return sharedSecret;
	}

	public void setSharedSecret(String sharedSecret) {
		this.sharedSecret = sharedSecret;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
