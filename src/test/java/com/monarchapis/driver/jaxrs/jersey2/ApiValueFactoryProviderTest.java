package com.monarchapis.driver.jaxrs.jersey2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.service.v1.CommandApi;
import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class ApiValueFactoryProviderTest {
	@Mock
	private ApiContext apiContext;

	@Mock
	private ServiceResolver serviceResolver;

	@Mock
	private ServiceApi serviceApi;

	@Mock
	private CommandApi commandApi;

	@Mock
	private Parameter parameter;

	private ApiValueFactoryProvider provider = new ApiValueFactoryProvider(
			(MultivaluedParameterExtractorProvider) null, (ServiceLocator) null);

	@Before
	public void setup() {
		ServiceResolver.setInstance(serviceResolver);
		when(serviceResolver.getService(ServiceApi.class)).thenReturn(serviceApi);
		when(serviceResolver.getService(CommandApi.class)).thenReturn(commandApi);

		ApiContext.setCurrent(apiContext);
	}

	@After
	public void teardown() {
		ApiContext.remove();
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testInjectApiContext() {
		when(parameter.getRawType()).then(new Answer<Class<?>>() {
			@Override
			public Class<?> answer(InvocationOnMock invocation) throws Throwable {
				return ApiContext.class;
			}
		});

		AbstractContainerRequestValueFactory<?> valueFactory = provider.createValueFactory(parameter);
		assertSame(apiContext, valueFactory.provide());
	}

	@Test
	public void testInjectServiceApi() {
		when(parameter.getRawType()).then(new Answer<Class<?>>() {
			@Override
			public Class<?> answer(InvocationOnMock invocation) throws Throwable {
				return ServiceApi.class;
			}
		});

		AbstractContainerRequestValueFactory<?> valueFactory = provider.createValueFactory(parameter);
		assertSame(serviceApi, valueFactory.provide());
	}

	@Test
	public void testInjectCommandApi() {
		when(parameter.getRawType()).then(new Answer<Class<?>>() {
			@Override
			public Class<?> answer(InvocationOnMock invocation) throws Throwable {
				return CommandApi.class;
			}
		});

		AbstractContainerRequestValueFactory<?> valueFactory = provider.createValueFactory(parameter);
		assertSame(commandApi, valueFactory.provide());
	}

	@Test
	public void testInjectUnknown() {
		when(parameter.getRawType()).then(new Answer<Class<?>>() {
			@Override
			public Class<?> answer(InvocationOnMock invocation) throws Throwable {
				return Object.class;
			}
		});

		AbstractContainerRequestValueFactory<?> valueFactory = provider.createValueFactory(parameter);
		assertNull(valueFactory);
	}
}
