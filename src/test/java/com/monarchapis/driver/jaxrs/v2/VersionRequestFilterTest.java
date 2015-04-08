package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;

@RunWith(MockitoJUnitRunner.class)
public class VersionRequestFilterTest {
	@Mock
	private ContainerRequestContext context;

	@Before
	@After
	public void teardown() {
		VersionHolder.remove();
	}

	@Test
	public void testVersionOnClass() throws IOException {
		VersionRequestFilter filter = new VersionRequestFilter("v1");
		assertNotNull(filter);
		assertNull(OperationNameHolder.getCurrent());
		filter.filter(context);
		assertSame("v1", VersionHolder.getCurrent());
	}
}
