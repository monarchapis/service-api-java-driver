package com.monarchapis.driver.service.v1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.Reference;
import com.monarchapis.driver.model.ServiceInfo;

@RunWith(MockitoJUnitRunner.class)
public class SingleServiceInfoResolverTest {
	@Mock
	private OpenApi openApi;

	@InjectMocks
	private SingleServiceInfoResolver resolver;

	private ServiceInfo serviceInfo;

	@Before
	public void setup() {
		serviceInfo = new ServiceInfo();
		serviceInfo.setEnvironment(new Reference("id", "name"));
		serviceInfo.setProvider(new Reference("id", "name"));
		serviceInfo.setService(new Reference("id", "name"));
		when(openApi.getServiceInfo("environment", "service", "provider")).thenReturn(serviceInfo);
	}

	@Test
	public void testDefaultConstructor() {
		new SingleServiceInfoResolver();
	}

	@Test
	public void testGetServiceInfo() {
		ServiceInfo actual = null;
		resolver.setEnvironmentName("environment");
		resolver.setServiceName("service");
		resolver.setProviderKey("provider");

		actual = resolver.getServiceInfo("/path");
		actual = resolver.getServiceInfo("/path");

		verify(openApi).getServiceInfo("environment", "service", "provider");
		assertSame(serviceInfo, actual);
	}
}
