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
