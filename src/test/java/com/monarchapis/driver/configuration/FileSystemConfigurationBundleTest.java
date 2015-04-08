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
