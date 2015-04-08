package com.monarchapis.driver.jaxrs.jersey2;

import static org.mockito.Mockito.*;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApiInjectionBinderTest {
	@Mock
	private DynamicConfiguration configuration;

	@Test
	public void testConfigure() {
		ApiInjectionBinder binder = new ApiInjectionBinder();
		binder = spy(binder);
		binder.bind(configuration);
		verify(binder).bind(ApiValueFactoryProvider.class);
		verify(binder).bind(ApiValueFactoryProvider.InjectionResolver.class);
	}
}
