package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.service.v1.CommandApi;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.api.core.HttpContext;

@RunWith(MockitoJUnitRunner.class)
public class CommandApiInjectableProviderTest {
	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	private CommandApi commandApi;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.getService(CommandApi.class)).thenReturn(commandApi);
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testGetValue() {
		CommandApiInjectableProvider provider = new CommandApiInjectableProvider();
		CommandApi actual = provider.getValue((HttpContext) null);
		assertSame(commandApi, actual);
	}
}
