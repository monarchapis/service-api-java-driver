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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.exception.NotFoundException;
import com.monarchapis.driver.model.OAuthEndpoints;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class SwaggerDocumentationResourceTest {
	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	protected ApiErrorFactory apiErrorFactory;

	@Mock
	private UriInfo uriInfo;

	private Swagger12DocumentationResource resource;

	@Before
	public void setup() throws URISyntaxException {
		OAuthEndpoints endpoints = new OAuthEndpoints();
		endpoints.setAuthorizationCodeRequestUrl("authorizationCodeRequestUrl");
		endpoints.setAuthorizationCodeTokenUrl("authorizationCodeTokenUrl");
		endpoints.setImplicitUrl("implicitUrl");
		endpoints.setPasswordUrl("passwordUrl");
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.required(OAuthEndpoints.class)).thenReturn(endpoints);
		when(serviceResolver.required(ApiErrorFactory.class)).thenReturn(apiErrorFactory);

		ApiError error = new ApiError(500, "message", "developerMesage", "errorCode", "moreInfo");
		when(apiErrorFactory.error(anyString())).thenReturn(error);
		when(apiErrorFactory.error(anyString(), anyString(), anyVararg())).thenReturn(error);

		when(uriInfo.getBaseUri()).thenReturn(new URI("http://api.company.com/test"));

		resource = new Swagger12DocumentationResource();
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testResourceListing() {
		String actual = resource.getResourceListing("v1", uriInfo);
		assertEquals("{ \"basePath\" : \"http://api.company.com/test\" }", actual);
	}

	@Test
	public void testApiDeclaration() throws IOException {
		String actual = resource.getApiDeclaration("v1", "test-resource", uriInfo);
		Reader reader = new StringReader(actual);
		List<String> lines = IOUtils.readLines(reader);
		assertEquals(7, lines.size());
		assertEquals("{", lines.get(0));
		assertEquals("\t\"basePath\" : \"http://api.company.com/test\",", lines.get(1));
		assertEquals("\t\"oauthPasswordUrl\" : \"passwordUrl\",", lines.get(2));
		assertEquals("\t\"oauthImplicitUrl\" : \"implicitUrl\",", lines.get(3));
		assertEquals("\t\"oauthAuthorizationCodeRequestUrl\" : \"authorizationCodeRequestUrl\",", lines.get(4));
		assertEquals("\t\"oauthAuthorizationCodeTokenUrl\" : \"authorizationCodeTokenUrl\"", lines.get(5));
		assertEquals("}", lines.get(6));
	}

	@Test
	public void testNotFound() {
		try {
			resource.getResourceListing("v999", uriInfo);
			fail("This should have thrown an exception");
		} catch (NotFoundException nfe) {
		}

		try {
			resource.getApiDeclaration("v999", "unknown", uriInfo);
			fail("This should have thrown an exception");
		} catch (NotFoundException nfe) {
		}
	}
}
