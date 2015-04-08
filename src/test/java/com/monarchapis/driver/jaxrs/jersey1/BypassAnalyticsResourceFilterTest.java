package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.sun.jersey.spi.container.ContainerRequest;

@RunWith(MockitoJUnitRunner.class)
public class BypassAnalyticsResourceFilterTest {
	@Mock
	private ContainerRequest containerRequest;

	@Before
	@After
	public void cleanup() {
		BypassAnalyticsHolder.remove();
	}

	@Test
	public void testVersionOnClass() {
		BypassAnalyticsResourceFilter filter = new BypassAnalyticsResourceFilter();
		assertNotNull(filter);
		assertNull(BypassAnalyticsHolder.getCurrent());
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame(true, BypassAnalyticsHolder.getCurrent());
	}
}
