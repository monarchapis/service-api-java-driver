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

import static org.junit.Assert.*;

import org.junit.Test;

import com.monarchapis.driver.annotation.ApiInject;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;

public class AbstractInjectableProviderTest {
	private TestProvider provider = new TestProvider();

	@Test
	public void testGetInjectable() {
		Injectable<Object> actual1 = provider.getInjectable((ComponentContext) null, (ApiInject) null, Object.class);
		assertSame(provider, actual1);

		Injectable<Object> actual2 = provider.getInjectable((ComponentContext) null, (ApiInject) null, String.class);
		assertNull(actual2);
	}

	@Test
	public void testGetScope() {
		assertEquals(ComponentScope.PerRequest, provider.getScope());
	}

	private static class TestProvider extends AbstractInjectableProvider<Object> {
		public TestProvider() {
			super(Object.class);
		}

		@Override
		public Object getValue(HttpContext c) {
			return "test";
		}
	}
}
