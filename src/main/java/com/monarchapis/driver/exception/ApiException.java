package com.monarchapis.driver.exception;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1974945309861445040L;

	public ApiException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ApiException(String message) {
		super(message);
	}
}