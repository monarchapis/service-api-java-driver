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
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.util.MediaTypeUtils;

/**
 * Handles mapping an <code>ApiErrorException</code> to a <code>Response</code>.
 * 
 * @author Phil Kedy
 */
@Provider
public class ApiResponseExceptionMapper implements ExceptionMapper<ApiErrorException> {
	@Override
	public Response toResponse(ApiErrorException e) {
		ApiError error = e.getError();

		return Response //
				.status(error.getStatus()) //
				.entity(error) //
				.type(MediaTypeUtils.getBestMediaType()) //
				.build();
	}
}