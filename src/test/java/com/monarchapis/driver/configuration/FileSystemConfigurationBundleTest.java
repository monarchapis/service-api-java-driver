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

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class FileSystemConfigurationBundleTest extends AbstractConfigurationBundleTest {
	@Before
	public void setup() {
		File baseDir = new File("src/test/resources/com/monarchapis/driver/configuration");
		setBundle(new FileSystemConfigurationBundle(baseDir, "Base1", "Base2"));
	}

	@Test
	public void testOtherConstructors() throws IOException {
		FileSystemConfigurationBundle bundle = new FileSystemConfigurationBundle();

		String[] prefixes = bundle.getPrefixes();
		assertNotNull(prefixes);
		assertEquals(0, prefixes.length);

		File baseDir = bundle.getBaseDir();
		File expected = new File(System.getProperty("user.dir"));
		assertNotNull(baseDir);
		assertEquals(expected.getCanonicalPath(), baseDir.getCanonicalPath());

		bundle.setPrefix("test");
		prefixes = bundle.getPrefixes();
		assertNotNull(prefixes);
		assertEquals(1, prefixes.length);
		assertEquals("test", prefixes[0]);
	}
}
