/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monarchapis.driver.exception;

import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.util.ServiceResolver;

/**
 * Wraps and API error in an exception.
 * 
 * @author Phil Kedy
 */
public class ApiErrorException extends ApiException {
	private static final long serialVersionUID = -6106308203401428275L;

	/**
	 * The underlying API error.
	 */
	private ApiError error;

	public ApiErrorException(String reason) {
		this(reason, null);
	}

	public ApiErrorException(String reason, Throwable t) {
		this(ServiceResolver.getInstance().required(ApiErrorFactory.class) //
				.error(reason), null);
	}

	public ApiErrorException(String reason, String template, String... args) {
		this(reason, (Throwable) null, template, args);
	}

	public ApiErrorException(String reason, Throwable t, String template, String... args) {
		this(ServiceResolver.getInstance().required(ApiErrorFactory.class) //
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
