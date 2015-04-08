package com.monarchapis.driver.exception;

public class ConflictException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public ConflictException() {
		super("conflict");
	}

	public ConflictException(Throwable t) {
		super("conflict", t);
	}

	public ConflictException(String template, String... args) {
		super("conflict", template, args);
	}

	public ConflictException(Throwable t, String template, String... args) {
		super("conflict", t, template, args);
	}
}