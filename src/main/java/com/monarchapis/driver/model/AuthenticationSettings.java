package com.monarchapis.driver.model;

public class AuthenticationSettings {
	private boolean delegateAuthorization = false;

	private boolean bypassRateLimiting = false;

	private boolean bypassAnalytics = false;

	public boolean isDelegateAuthorization() {
		return delegateAuthorization;
	}

	public void setDelegateAuthorization(boolean delegateAuthorization) {
		this.delegateAuthorization = delegateAuthorization;
	}

	public boolean isBypassRateLimiting() {
		return bypassRateLimiting;
	}

	public void setBypassRateLimiting(boolean bypassRateLimiting) {
		this.bypassRateLimiting = bypassRateLimiting;
	}

	public boolean isBypassAnalytics() {
		return bypassAnalytics;
	}

	public void setBypassAnalytics(boolean bypassAnalytics) {
		this.bypassAnalytics = bypassAnalytics;
	}
}
