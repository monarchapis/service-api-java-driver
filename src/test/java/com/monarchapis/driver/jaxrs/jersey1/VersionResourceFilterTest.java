package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.VersionHolder;
import com.sun.jersey.spi.container.ContainerRequest;

@RunWith(MockitoJUnitRunner.class)
public class VersionResourceFilterTest {
	@Mock
	private ContainerRequest containerRequest;
	
	@Before
	@After
	public void teardown() {
		VersionHolder.remove();
	}
	
	@Test
	public void testVersionOnClass() {
		VersionResourceFilter filter = new VersionResourceFilter("v1");
		assertNotNull(filter);
		assertNull(VersionHolder.getCurrent());
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame("v1", VersionHolder.getCurrent());
	}
}
