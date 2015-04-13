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

package com.monarchapis.driver.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SpringServiceResolverTest {
	@Mock
	private ApplicationContext applicationContext;

	@InjectMocks
	private SpringServiceResolver resolver;

	@AfterClass
	public static void destroy() {
		ServiceResolver.setInstance(null);
	}

	@Before
	public void setup() {
		resolver.setApplicationContext(applicationContext);

		when(applicationContext.getBean(String.class)).thenReturn("test");
		when(applicationContext.getBean(Boolean.class)).thenThrow(new NoSuchBeanDefinitionException("test"));
	}

	@Test
	public void testInitDestroy() {
		assertNull(ServiceResolver.getInstance());

		resolver.init();
		assertSame(resolver, ServiceResolver.getInstance());

		resolver.destroy();
		assertNull(ServiceResolver.getInstance());
	}

	@Test
	public void testBeanFound() {
		assertEquals("test", resolver.required(String.class));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testBeanNotFound() {
		resolver.required(Boolean.class);
	}

	@Test
	public void testOptionalBeanFound() {
		Optional<String> actual = resolver.optional(String.class);
		assertNotNull(actual);
		assertTrue(actual.isPresent());
		assertEquals("test", actual.get());
	}

	@Test
	public void testOptionalBeanNotFound() {
		Optional<Boolean> actual = resolver.optional(Boolean.class);
		assertNotNull(actual);
		assertFalse(actual.isPresent());
	}
}
