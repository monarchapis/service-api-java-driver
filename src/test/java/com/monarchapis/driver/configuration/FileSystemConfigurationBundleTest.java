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

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class FileSystemConfigurationBundleTest extends AbstractConfigurationBundleTest {
	private FileSystemConfigurationBundle bundle1;
	private FileSystemConfigurationBundle bundle2;

	@Before
	public void setup() {
		File baseDir = new File("src/test/resources/com/monarchapis/driver/configuration");
		bundle1 = new FileSystemConfigurationBundle(baseDir, "Base1");
		bundle2 = new FileSystemConfigurationBundle(baseDir, "Base2");
		setBundle(new MultiConfigurationBundle(bundle1, bundle2));
	}

	@Test
	public void testOtherConstructors() {

	}
}
