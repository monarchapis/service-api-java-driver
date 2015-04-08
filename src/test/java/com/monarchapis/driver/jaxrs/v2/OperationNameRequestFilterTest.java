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

@RunWith(MockitoJUnitRunner.class)
public class OperationNameRequestFilterTest {
	@Mock
	private ContainerRequestContext context;

	@Before
	@After
	public void teardown() {
		OperationNameHolder.remove();
	}

	@Test
	public void testVersionOnClass() throws IOException {
		OperationNameRequestFilter filter = new OperationNameRequestFilter("someOperation");
		assertNotNull(filter);
		assertNull(OperationNameHolder.getCurrent());
		filter.filter(context);
		assertSame("someOperation", OperationNameHolder.getCurrent());
	}
}
