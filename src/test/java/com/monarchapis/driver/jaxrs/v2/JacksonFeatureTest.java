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

package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.CommonProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@RunWith(MockitoJUnitRunner.class)
public class JacksonFeatureTest {
	@Mock
	private FeatureContext context;

	@Mock
	private Configuration configuration;

	@Before
	public void setup() {
		when(context.getConfiguration()).thenReturn(configuration);
		when(configuration.getRuntimeType()).thenReturn(RuntimeType.SERVER);
	}

	@Test
	public void testConfigure() {
		JacksonFeature feature = new JacksonFeature();

		boolean result = feature.configure(context);
		assertEquals(true, result);
		verify(context).property(
				CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.' + RuntimeType.SERVER.name().toLowerCase(), true);
		verify(context).register(JacksonJaxbJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
	}
}
