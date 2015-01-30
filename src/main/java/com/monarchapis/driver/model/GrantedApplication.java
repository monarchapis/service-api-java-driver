package com.monarchapis.driver.model;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GrantedApplication extends Application {
	private String tokenId;
	private Set<String> permissions;
	private List<Message> permissionMessages;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public List<Message> getPermissionMessages() {
		return permissionMessages;
	}

	public void setPermissionMessages(List<Message> permissionMessages) {
		this.permissionMessages = permissionMessages;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
