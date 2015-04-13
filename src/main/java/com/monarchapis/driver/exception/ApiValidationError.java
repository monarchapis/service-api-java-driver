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

import java.util.Set;

import org.joda.time.DateTime;

/**
 * A specialized API error that includes validation errors (e.g. for form
 * validation, etc.).
 * 
 * @author Phil Kedy
 */
public class ApiValidationError extends ApiError {
	private static int BAD_REQUEST = 400;

	/**
	 * The set of validation errors.
	 */
	private Set<ValidationError> validationErrors;

	public ApiValidationError() {
		super();
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode,
			String moreInfo, DateTime time) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode, moreInfo, time);
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode, String moreInfo) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode, moreInfo);
	}

	public ApiValidationError(String reason, String message, String developerMessage, String errorCode) {
		super(BAD_REQUEST, reason, message, developerMessage, errorCode);
	}

	public Set<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(Set<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}
}
