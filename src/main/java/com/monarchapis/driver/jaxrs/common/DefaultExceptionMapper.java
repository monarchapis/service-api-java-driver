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
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles mapping a general exception to a <code>Response</code> with an
 * "internalError" reason.
 * 
 * @author Phil Kedy
 */
@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper<Exception> {
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	@Override
	protected String getReason() {
		return "internalError";
	}

	@Override
	public Response toResponse(Exception exception) {
		logger.error("An uncaught exception occurred", exception);

		return super.toResponse(exception);
	}
}