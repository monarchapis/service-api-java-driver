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

package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;

@RunWith(MockitoJUnitRunner.class)
public class VersionRequestFilterTest {
	@Mock
	private ContainerRequestContext context;

	@Before
	@After
	public void teardown() {
		VersionHolder.remove();
	}

	@Test
	public void testVersionOnClass() throws IOException {
		VersionRequestFilter filter = new VersionRequestFilter("v1");
		assertNotNull(filter);
		assertNull(OperationNameHolder.getCurrent());
		filter.filter(context);
		assertSame("v1", VersionHolder.getCurrent());
	}
}
