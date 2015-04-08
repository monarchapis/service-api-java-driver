package com.monarchapis.driver.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceResolverTest {
	@Mock
	private ServiceResolver instance;

	@Before
	@After
	public void destroy() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testStaticInstance() {
		assertNull(ServiceResolver.getInstance());

		ServiceResolver.setInstance(instance);
		assertSame(instance, ServiceResolver.getInstance());

		ServiceResolver.setInstance(null);
		assertNull(ServiceResolver.getInstance());
	}
}
