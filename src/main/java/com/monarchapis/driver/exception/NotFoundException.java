package com.monarchapis.driver.exception;

public class NotFoundException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public NotFoundException() {
		super("notFound");
	}

	public NotFoundException(Throwable t) {
		super("notFound", t);
	}

	public NotFoundException(String template, String... args) {
		super("notFound", template, args);
	}

	public NotFoundException(Throwable t, String template, String... args) {
		super("notFound", t, template, args);
	}
}