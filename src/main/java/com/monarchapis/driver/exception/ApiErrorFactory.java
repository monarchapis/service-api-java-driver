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

/**
 * An interface for creating API errors and throwing API error exception without
 * having to hard code error details in the API methods.
 * 
 * @author Phil Kedy
 */
public interface ApiErrorFactory {
	/**
	 * Returns the API error based on the error reason.
	 * 
	 * @param errorReason
	 *            The error reason
	 * @return the API error
	 */
	ApiError error(String errorReason);

	/**
	 * Returns the API error based on the error reason, template, and arguments.
	 * 
	 * @param errorReason
	 *            The error reason
	 * @param template
	 *            The template to select for creating the API error
	 * @param args
	 *            The optional arguments to be used in formatting the error
	 *            string values.
	 * @return the API error based on the template and arguments
	 */
	ApiError error(String errorReason, String template, Object... args);

	/**
	 * Returns the API error exception based on the error reason.
	 * 
	 * @param errorReason
	 *            The error reason
	 * @return the API error exception
	 */
	ApiErrorException exception(String errorReason);

	/**
	 * Returns the API error exception based on the error reason, template, and
	 * arguments.
	 * 
	 * @param errorReason
	 *            The error reason
	 * @param template
	 *            The template to select for creating the API error
	 * @param args
	 *            The optional arguments to be used in formatting the error
	 *            string values.
	 * @return the API error exception based on the template and arguments
	 */
	ApiErrorException exception(String errorReason, String template, Object... args);
}
