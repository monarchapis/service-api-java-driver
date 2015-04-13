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

package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.VersionHolder;
import com.sun.jersey.spi.container.ContainerRequest;

@RunWith(MockitoJUnitRunner.class)
public class VersionResourceFilterTest {
	@Mock
	private ContainerRequest containerRequest;
	
	@Before
	@After
	public void teardown() {
		VersionHolder.remove();
	}
	
	@Test
	public void testVersionOnClass() {
		VersionResourceFilter filter = new VersionResourceFilter("v1");
		assertNotNull(filter);
		assertNull(VersionHolder.getCurrent());
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame("v1", VersionHolder.getCurrent());
	}
}
