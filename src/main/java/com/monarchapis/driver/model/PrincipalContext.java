package com.monarchapis.driver.model;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrincipalContext {
	private String id;
	private String context;
	private Map<String, Set<String>> claims;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Map<String, Set<String>> getClaims() {
		return claims;
	}

	public void setClaims(Map<String, Set<String>> claims) {
		this.claims = claims;
	}

	public boolean hasClaim(String claimType) {
		return hasClaim(claimType, null);
	}

	public boolean hasClaim(String claimType, String claimValue) {
		if (claims == null) {
			return false;
		}

		Set<String> values = claims.get(claimType);

		if (values == null) {
			return false;
		}

		return StringUtils.isEmpty(claimValue) || values.contains(claimValue);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
