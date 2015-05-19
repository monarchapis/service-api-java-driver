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

package com.monarchapis.driver.configuration;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ResourceConfigurationBundleTest extends AbstractConfigurationBundleTest {
	private ResourceConfigurationBundle bundle;

	@Before
	public void setup() {
		bundle = new ResourceConfigurationBundle( //
				"/com/monarchapis/driver/configuration/Base1", //
				"com/monarchapis/driver/configuration/Base2");
		setBundle(bundle);
	}

	@Test
	public void testOtherConstructors() {
		bundle = new ResourceConfigurationBundle();
		assertSame(ResourceConfigurationBundle.class.getClassLoader(), bundle.getBundleClassLoader());
		assertEquals(Collections.emptyList(), Lists.newArrayList(bundle.getBasenames()));

		bundle = new ResourceConfigurationBundle(this.getClass().getClassLoader());
		assertSame(this.getClass().getClassLoader(), bundle.getBundleClassLoader());
	}

	@Test
	public void testSetSingleBasename() {
		bundle.setBasename("com/monarchapis/driver/configuration/Base1");
		String[] actual = bundle.getBasenames();
		assertEquals(//
				Lists.newArrayList("com/monarchapis/driver/configuration/Base1"), //
				Lists.newArrayList(actual));
	}

	@Test
	public void testGetBundleClassloader() {
		assertSame(ResourceConfigurationBundle.class.getClassLoader(), bundle.getBundleClassLoader());
		bundle.setBundleClassLoader(this.getClass().getClassLoader());
		assertSame(this.getClass().getClassLoader(), bundle.getBundleClassLoader());
	}
}
