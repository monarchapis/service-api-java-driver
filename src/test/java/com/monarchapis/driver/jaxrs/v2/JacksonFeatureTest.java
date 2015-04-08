package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.CommonProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@RunWith(MockitoJUnitRunner.class)
public class JacksonFeatureTest {
	@Mock
	private FeatureContext context;

	@Mock
	private Configuration configuration;

	@Before
	public void setup() {
		when(context.getConfiguration()).thenReturn(configuration);
		when(configuration.getRuntimeType()).thenReturn(RuntimeType.SERVER);
	}

	@Test
	public void testConfigure() {
		JacksonFeature feature = new JacksonFeature();

		boolean result = feature.configure(context);
		assertEquals(true, result);
		verify(context).property(
				CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.' + RuntimeType.SERVER.name().toLowerCase(), true);
		verify(context).register(JacksonJaxbJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
	}
}
