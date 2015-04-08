package com.monarchapis.driver.exception;

public class ForbiddenException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public ForbiddenException() {
		super("forbidden");
	}

	public ForbiddenException(Throwable t) {
		super("forbidden", t);
	}

	public ForbiddenException(String template, String... args) {
		super("forbidden", template, args);
	}

	public ForbiddenException(Throwable t, String template, String... args) {
		super("forbidden", t, template, args);
	}
}