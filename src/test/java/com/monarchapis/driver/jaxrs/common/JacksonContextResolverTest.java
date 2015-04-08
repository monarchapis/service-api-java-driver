package com.monarchapis.driver.jaxrs.common;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonContextResolverTest {
	@Test
	public void testGetContext() {
		JacksonContextResolver resolver = new JacksonContextResolver();
		ObjectMapper mapper = resolver.getContext(ObjectMapper.class);
		assertNotNull(mapper);
	}
}
