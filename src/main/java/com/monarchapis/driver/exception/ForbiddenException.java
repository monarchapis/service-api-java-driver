package com.monarchapis.driver.exception;

public class ForbiddenException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public ForbiddenException() {
		this("Access denied", "Your client is not authorized to make this request.", "SEC-0001", null);
	}

	public ForbiddenException(String message, String developerMessage, String errorCode, String moreInfo) {
		super(403, "forbidden", message, developerMessage, errorCode, moreInfo);
	}
}
