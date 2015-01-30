package com.monarchapis.driver.exception;

import java.util.Set;

import org.joda.time.DateTime;

public class ApiValidationError extends ApiError {
	private static int BAD_REQUEST = 400;

	private Set<ValidationError> validationErrors;

	public ApiValidationError() {
		super();
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode,
			String moreInfo, DateTime time) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode, moreInfo, time);
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode, String moreInfo) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode, moreInfo);
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode);
	}

	public Set<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(Set<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}
}
