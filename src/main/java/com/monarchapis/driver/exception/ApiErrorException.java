package com.monarchapis.driver.exception;

import org.joda.time.DateTime;

import com.monarchapis.driver.model.ErrorHolder;

public class ApiErrorException extends ApiException {
	private static final long serialVersionUID = -6106308203401428275L;

	private ApiError error;

	public ApiErrorException(ApiError error) {
		this(error, null);
	}

	public ApiErrorException(ApiError error, Throwable t) {
		super(error.getMessage(), t);

		this.error = error;

		ErrorHolder.setCurrent(error);
	}

	public ApiErrorException(int status, String reason, String message, String developerMessage, String errorCode,
			String moreInfo) {
		this(new ApiError(status, reason, message, developerMessage, errorCode, moreInfo));
	}

	public ApiErrorException(int status, String reason, String message, String developerMessage, String errorCode,
			String moreInfo, Throwable t) {
		this(new ApiError(status, reason, message, developerMessage, errorCode, moreInfo), t);
	}

	public ApiError getError() {
		return error;
	}

	public int getCode() {
		return error.getCode();
	}

	public String getReason() {
		return error.getReason();
	}

	public String getDeveloperMessage() {
		return error.getDeveloperMessage();
	}

	public DateTime getTime() {
		return error.getTime();
	}

	public String getErrorCode() {
		return error.getErrorCode();
	}

	public String getMoreInfo() {
		return error.getMoreInfo();
	}
}
