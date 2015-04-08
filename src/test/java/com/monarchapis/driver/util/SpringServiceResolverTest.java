package com.monarchapis.driver.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
public class SpringServiceResolverTest {
	@Mock
	private ApplicationContext applicationContext;

	@InjectMocks
	private SpringServiceResolver resolver;

	@AfterClass
	public static void destroy() {
		ServiceResolver.setInstance(null);
	}

	@Before
	public void setup() {
		resolver.setApplicationContext(applicationContext);

		when(applicationContext.getBean(String.class)).thenReturn("test");
		when(applicationContext.getBean(Boolean.class)).thenThrow(new NoSuchBeanDefinitionException("test"));
	}

	@Test
	public void testInitDestroy() {
		assertNull(ServiceResolver.getInstance());

		resolver.init();
		assertSame(resolver, ServiceResolver.getInstance());

		resolver.destroy();
		assertNull(ServiceResolver.getInstance());
	}

	@Test
	public void testBeanFound() {
		assertEquals("test", resolver.getService(String.class));
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testBeanNotFound() {
		resolver.getService(Boolean.class);
	}

	@Test
	public void testOptionalBeanFound() {
		Optional<String> actual = resolver.getOptional(String.class);
		assertNotNull(actual);
		assertTrue(actual.isPresent());
		assertEquals("test", actual.get());
	}

	@Test
	public void testOptionalBeanNotFound() {
		Optional<Boolean> actual = resolver.getOptional(Boolean.class);
		assertNotNull(actual);
		assertFalse(actual.isPresent());
	}
}
