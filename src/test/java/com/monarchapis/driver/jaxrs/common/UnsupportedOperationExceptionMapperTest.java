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

import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;

@RunWith(MockitoJUnitRunner.class)
public class UnsupportedOperationExceptionMapperTest extends AbstractExceptionMapperTest {
	@Test
	public void testToResponse() {
		UnsupportedOperationExceptionMapper mapper = new UnsupportedOperationExceptionMapper();
		ApiError error = new ApiError(400, "reason", "message", "developerMessage", "errorCode", "moreInfo");
		when(apiErrorFactory.error("methodNotAllowed", null, new Object[0])).thenReturn(error);
		Response response = mapper.toResponse(new UnsupportedOperationException());
		assertResponse(error, response);
	}
}
