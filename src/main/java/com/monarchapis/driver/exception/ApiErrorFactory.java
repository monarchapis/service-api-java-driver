package com.monarchapis.driver.exception;

public interface ApiErrorFactory {
	ApiError error(String errorReason);

	ApiError error(String errorReason, String template, Object... args);

	ApiErrorException exception(String errorReason);

	ApiErrorException exception(String errorReason, String template, Object... args);
}
