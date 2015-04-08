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
