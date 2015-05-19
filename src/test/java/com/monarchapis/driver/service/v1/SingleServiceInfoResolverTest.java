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

import com.google.common.base.Optional;
import com.monarchapis.api.v1.client.OpenApi;
import com.monarchapis.api.v1.client.OpenResource;
import com.monarchapis.api.v1.model.Reference;
import com.monarchapis.api.v1.model.ServiceInfo;

@RunWith(MockitoJUnitRunner.class)
public class SingleServiceInfoResolverTest {
	@Mock
	private OpenApi openApi;

	@Mock
	private OpenResource openResource;

	@InjectMocks
	private SingleServiceInfoResolver resolver;

	private ServiceInfo serviceInfo;

	@Before
	public void setup() {
		serviceInfo = new ServiceInfo();
		serviceInfo.setEnvironment(reference("id", "name"));
		serviceInfo.setProvider(Optional.of(reference("id", "name")));
		serviceInfo.setService(Optional.of(reference("id", "name")));
		when(openApi.getOpenResource()).thenReturn(openResource);
		when(openResource.getServiceInfo("environment", "service", "provider")).thenReturn(serviceInfo);
	}

	private Reference reference(String id, String name) {
		Reference ref = new Reference();
		ref.setId(id);
		ref.setName(name);

		return ref;
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

		verify(openResource).getServiceInfo("environment", "service", "provider");
		assertSame(serviceInfo, actual);
	}
}
