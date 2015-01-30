package com.monarchapis.driver.exception;

public class ConflictException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public ConflictException() {
		this("Conflict", "There was a conflict encountered in processing your request.", "CON-0001", null);
	}

	public ConflictException(String message, String developerMessage, String errorCode, String moreInfo) {
		super(409, "conflict", message, developerMessage, errorCode, moreInfo);
	}
}
