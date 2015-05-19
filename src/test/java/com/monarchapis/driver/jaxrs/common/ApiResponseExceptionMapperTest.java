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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.servlet.ApiRequest;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseExceptionMapperTest {
	@Mock
	private ApiRequest request;

	private ApiError error;
	private ApiErrorException e;
	private ApiResponseExceptionMapper mapper;

	@Before
	public void setup() {
		ApiRequest.setCurrent(request);
		when(request.getHeader("Accept")).thenReturn("application/json");

		error = new ApiError(404, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		e = new ApiErrorException(error);
		mapper = new ApiResponseExceptionMapper();
	}

	@After
	public void teardown() {
		ApiRequest.remove();
	}

	@Test
	public void testToResponse() {
		Response response = mapper.toResponse(e);
		assertNotNull(response);
		assertEquals(error.getStatus(), response.getStatus());
		assertEquals(error, response.getEntity());
	}
}
