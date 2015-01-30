package com.monarchapis.driver.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Client {
	private String id;
	private String apiKey;
	private Long expiration;
	private boolean autoAuthorize;
	private boolean allowWebView;
	private boolean allowPopup;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	public boolean isAutoAuthorize() {
		return autoAuthorize;
	}

	public void setAutoAuthorize(boolean autoAuthorize) {
		this.autoAuthorize = autoAuthorize;
	}

	public boolean isAllowWebView() {
		return allowWebView;
	}

	public void setAllowWebView(boolean allowWebViews) {
		this.allowWebView = allowWebViews;
	}

	public boolean isAllowPopup() {
		return allowPopup;
	}

	public void setAllowPopup(boolean allowPopup) {
		this.allowPopup = allowPopup;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
