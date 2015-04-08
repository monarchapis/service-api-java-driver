package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.api.core.HttpContext;

@RunWith(MockitoJUnitRunner.class)
public class ServiceApiInjectableProviderTest {
	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	private ServiceApi serviceApi;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.getService(ServiceApi.class)).thenReturn(serviceApi);
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testGetValue() {
		ServiceApiInjectableProvider provider = new ServiceApiInjectableProvider();
		ServiceApi actual = provider.getValue((HttpContext) null);
		assertSame(serviceApi, actual);
	}
}
