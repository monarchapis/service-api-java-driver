package com.monarchapis.driver.exception;

import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.util.ServiceResolver;

public class ApiErrorException extends ApiException {
	private static final long serialVersionUID = -6106308203401428275L;

	private ApiError error;

	public ApiErrorException(String reason) {
		this(reason, null);
	}

	public ApiErrorException(String reason, Throwable t) {
		this(ServiceResolver.getInstance().getService(ApiErrorFactory.class) //
				.error(reason), null);
	}

	public ApiErrorException(String reason, String template, String... args) {
		this(reason, (Throwable) null, template, args);
	}

	public ApiErrorException(String reason, Throwable t, String template, String... args) {
		this(ServiceResolver.getInstance().getService(ApiErrorFactory.class) //
				.error(reason, template, (Object[]) args), t);
	}

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
}
