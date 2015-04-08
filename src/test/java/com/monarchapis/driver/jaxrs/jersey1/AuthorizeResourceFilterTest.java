package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.util.ServiceResolver;
import com.sun.jersey.spi.container.ContainerRequest;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizeResourceFilterTest {
	@Mock
	protected ServiceResolver serviceResolver;

	@Mock
	private Authenticator authenticator;

	@Mock
	private ContainerRequest request;

	@Mock
	private Claim claim;

	private AuthorizeResourceFilter filter;

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.getService(Authenticator.class)).thenReturn(authenticator);

		filter = new AuthorizeResourceFilter(//
				new String[] { "client" }, //
				true, //
				new String[] { "delegated" }, //
				new Claim[] { claim }, //
				new BigDecimal("1"));
	}

	@After
	public void teardown() {
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testGetters() {
		assertEquals("client", filter.getClient()[0]);
		assertEquals("delegated", filter.getDelegated()[0]);
		assertEquals(claim, filter.getClaims()[0]);
		assertEquals(true, filter.isUser());
		assertEquals(new BigDecimal("1"), filter.getRequestWeight());
	}

	@Test
	public void testJerseyConstructs() {
		assertSame(filter, filter.getRequestFilter());
		assertNull(filter.getResponseFilter());
	}

	@Test
	public void testAuthorization() {
		filter.filter(request);
		verify(authenticator).performAccessChecks(new BigDecimal("1"), //
				new String[] { "client" }, //
				new String[] { "delegated" }, //
				true, //
				new Claim[] { claim });
	}
}
