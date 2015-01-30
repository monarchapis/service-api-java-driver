package com.monarchapis.driver.exception;

public class InvalidAccessTokenException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public InvalidAccessTokenException() {
		this("Access denied", "A valid access token was not found in your request", "SEC-0002", null);
	}

	public InvalidAccessTokenException(String message, String developerMessage, String errorCode, String moreInfo) {
		super(401, "invalidAccessToken", message, developerMessage, errorCode, moreInfo);
	}
}
