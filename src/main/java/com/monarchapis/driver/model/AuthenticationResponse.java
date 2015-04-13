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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an authentication response for the API request.
 * 
 * @author Phil Kedy
 */
public class AuthenticationResponse {
	/**
	 * The HTTP status code.
	 */
	private int code;

	/**
	 * The reason if an error was encountered.
	 */
	private String reason;

	/**
	 * The error message.
	 */
	private String message;

	/**
	 * The developer message.
	 */
	private String developerMessage;

	/**
	 * The error code.
	 */
	private String errorCode;

	/**
	 * The list of HTTP headers to relay back to the client.
	 */
	private List<HttpHeader> responseHeaders;

	/**
	 * If successful, the ApiContext.
	 */
	private ApiContext context;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<HttpHeader> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(List<HttpHeader> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public ApiContext getContext() {
		return context;
	}

	public void setContext(ApiContext context) {
		this.context = context;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
