package com.monarchapis.driver.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TokenRequest extends TokenBase implements Serializable {
	private static final long serialVersionUID = 4570272326916068744L;

	private String apiKey;
	private String authorizationScheme;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAuthorizationScheme() {
		return authorizationScheme;
	}

	public void setAuthorizationScheme(String authorizationScheme) {
		this.authorizationScheme = authorizationScheme;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
