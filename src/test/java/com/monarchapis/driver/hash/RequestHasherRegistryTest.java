package com.monarchapis.driver.hash;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.exception.ApiException;

@RunWith(MockitoJUnitRunner.class)
public class RequestHasherRegistryTest {
	@Mock
	private RequestHasher hasher;

	private RequestHasherRegistry registry;

	@Before
	public void setup() {
		when(hasher.getName()).thenReturn("test");
		registry = new RequestHasherRegistry(hasher);
	}

	@Test
	public void testGetAllHashers() {
		List<RequestHasher> hashers = registry.getRequestHashers();
		assertNotNull(hashers);
		assertEquals(1, hashers.size());
		assertEquals(hasher, hashers.get(0));
	}

	@Test
	public void testGetKnownHasher() {
		assertEquals(hasher, registry.getRequestHasher("test"));
	}

	@Test
	public void testGetUnknownHasher() {
		try {
			registry.getRequestHasher("unknown");
			fail("This should have thrown an exception");
		} catch (ApiException apie) {
			assertEquals("Request hasher unknown is not registered", apie.getMessage());
		}
	}
}
