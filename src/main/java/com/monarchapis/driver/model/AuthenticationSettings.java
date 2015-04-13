/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monarchapis.driver.model;

/**
 * Represents authentication settings.
 * 
 * @author Phil Kedy
 */
public class AuthenticationSettings {
	/**
	 * Flag that tells Monarch to perform authorization instead of the API
	 * itself.
	 */
	private boolean delegateAuthorization = false;

	/**
	 * Flag that tells Monarch to bypass rate limiting. This is typically set to
	 * true if you are using the API Gateway.
	 */
	private boolean bypassRateLimiting = false;

	/**
	 * Flag that tells the API to bypass sending traffic statistics to Monarch.
	 */
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
