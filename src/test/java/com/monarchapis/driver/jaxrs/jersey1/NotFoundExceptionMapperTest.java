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

package com.monarchapis.driver.jaxrs.jersey1;

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.jaxrs.common.AbstractExceptionMapperTest;
import com.sun.jersey.api.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class NotFoundExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
		ApiError error = new ApiError(404, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("notFound", null)).thenReturn(error);
		NotFoundException e = Mockito.mock(NotFoundException.class);
		Response response = mapper.toResponse(e);
		assertResponse(error, response);
	}
}
