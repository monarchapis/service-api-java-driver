package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;

import org.junit.Test;

import com.monarchapis.driver.model.ApiContext;
import com.sun.jersey.api.core.HttpContext;

public class ApiContextInjectableProviderTest {
	@Test
	public void testGetValue() {
		ApiContext expected = new ApiContext();
		ApiContext.setCurrent(expected);
		ApiContextInjectableProvider provider = new ApiContextInjectableProvider();
		ApiContext actual = provider.getValue((HttpContext) null);
		assertEquals(expected, actual);
	}
}
