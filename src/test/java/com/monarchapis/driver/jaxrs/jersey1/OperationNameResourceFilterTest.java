package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.monarchapis.driver.model.OperationNameHolder;
import com.sun.jersey.spi.container.ContainerRequest;

public class OperationNameResourceFilterTest {
	@Mock
	private ContainerRequest containerRequest;

	@Before
	@After
	public void teardown() {
		OperationNameHolder.remove();
	}

	@Test
	public void testVersionOnClass() {
		OperationNameResourceFilter filter = new OperationNameResourceFilter("someOperation");
		assertNotNull(filter);
		assertNull(OperationNameHolder.getCurrent());
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame("someOperation", OperationNameHolder.getCurrent());
	}
}
