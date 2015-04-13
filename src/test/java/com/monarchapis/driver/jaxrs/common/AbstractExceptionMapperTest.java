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
import org.mockito.Mock;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.util.ServiceResolver;

public class AbstractExceptionMapperTest {
	@Mock
	protected ApiRequest request;

	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	protected ApiErrorFactory apiErrorFactory;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.required(ApiErrorFactory.class)).thenReturn(apiErrorFactory);

		ApiRequest.setCurrent(request);
		when(request.getHeader("Accept")).thenReturn("application/json");
	}

	@After
	public void teardown() {
		ApiRequest.remove();
		ErrorHolder.remove();
		ServiceResolver.setInstance(null);
	}

	protected void assertResponse(ApiError error, Response response) {
		assertNotNull(response);
		assertEquals(error.getStatus(), response.getStatus());
		ApiError responseError = (ApiError) response.getEntity();
		assertEquals(error, responseError);
	}
}
