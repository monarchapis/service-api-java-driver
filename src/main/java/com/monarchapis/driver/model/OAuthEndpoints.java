package com.monarchapis.driver.model;

public class OAuthEndpoints {
	private String authorizationCodeRequestUrl;
	private String authorizationCodeTokenUrl;
	private String implicitUrl;
	private String passwordUrl;

	public String getAuthorizationCodeRequestUrl() {
		return authorizationCodeRequestUrl;
	}

	public void setAuthorizationCodeRequestUrl(String authorizationCodeRequestUrl) {
		this.authorizationCodeRequestUrl = authorizationCodeRequestUrl;
	}

	public String getAuthorizationCodeTokenUrl() {
		return authorizationCodeTokenUrl;
	}

	public void setAuthorizationCodeTokenUrl(String authorizationCodeTokenUrl) {
		this.authorizationCodeTokenUrl = authorizationCodeTokenUrl;
	}

	public String getImplicitUrl() {
		return implicitUrl;
	}

	public void setImplicitUrl(String implicitUrl) {
		this.implicitUrl = implicitUrl;
	}

	public String getPasswordUrl() {
		return passwordUrl;
	}

	public void setPasswordUrl(String passwordUrl) {
		this.passwordUrl = passwordUrl;
	}
}
