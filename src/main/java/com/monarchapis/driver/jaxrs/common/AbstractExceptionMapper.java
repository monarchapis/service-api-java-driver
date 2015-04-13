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

package com.monarchapis.driver.jaxrs.common;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.util.MediaTypeUtils;
import com.monarchapis.driver.util.ServiceResolver;

/**
 * Provides support for exceptions to populate an <code>ApiError</code> via the
 * <code>ApiErrorFactory</code> and return it as the response entity.
 * 
 * @author Phil Kedy
 */
public abstract class AbstractExceptionMapper<T extends Exception> implements ExceptionMapper<T> {
	/**
	 * A reusable reference for empty arguments.
	 */
	private static final Object[] NO_ARGUMENTS = new Object[0];

	@Override
	public Response toResponse(T exception) {
		ApiErrorFactory apiErrorFactory = ServiceResolver.getInstance().required(ApiErrorFactory.class);

		ApiError error = apiErrorFactory.error(//
				getReason(exception), //
				getTemplate(exception), //
				getArguments(exception));
		overrideErrorProperties(error, exception);

		return Response //
				.status(Response.Status.fromStatusCode(error.getStatus())) //
				.entity(error) //
				.type(MediaTypeUtils.getBestMediaType()) //
				.build();
	}

	protected String getReason(T exception) {
		return getReason();
	}

	protected String getReason() {
		throw new UnsupportedOperationException("Please override getReason() or getReason(String)");
	}

	protected String getTemplate(T exception) {
		return null;
	}

	protected Object[] getArguments(T exception) {
		return NO_ARGUMENTS;
	}

	protected void overrideErrorProperties(ApiError error, T exception) {
	}
}