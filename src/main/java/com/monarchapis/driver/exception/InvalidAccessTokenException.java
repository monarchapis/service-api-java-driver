package com.monarchapis.driver.exception;

public class InvalidAccessTokenException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public InvalidAccessTokenException() {
		super("invalidAccessToken");
	}

	public InvalidAccessTokenException(Throwable t) {
		super("invalidAccessToken", t);
	}

	public InvalidAccessTokenException(String template, String... args) {
		super("invalidAccessToken", template, args);
	}

	public InvalidAccessTokenException(Throwable t, String template, String... args) {
		super("invalidAccessToken", t, template, args);
	}
}