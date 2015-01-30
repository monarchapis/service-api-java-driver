package com.monarchapis.driver.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthenticationResponse {
	private int code;

	private String reason;

	private String message;

	private String developerMessage;

	private String errorCode;

	private List<HttpHeader> responseHeaders;

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
