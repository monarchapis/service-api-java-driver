package com.monarchapis.driver.jaxrs.jersey2;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.FeatureContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApiFeatureTest {
	@Mock
	private FeatureContext featureContext;

	@Test
	public void testConfigure() {
		ApiFeature feature = new ApiFeature();
		feature.configure(featureContext);
		verify(featureContext).register(any(ApiInjectionBinder.class));
	}
}
