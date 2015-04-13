/*
 * Copyright (C) 2015 CapTech Ventures, Inc.
 * (http://www.captechconsulting.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
