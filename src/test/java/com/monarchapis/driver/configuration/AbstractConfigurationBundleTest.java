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

import org.junit.Test;

import com.google.common.base.Optional;

public abstract class AbstractConfigurationBundleTest {
	protected ConfigurationBundle bundle;

	protected String[] noVariants = new String[0];
	protected String[] enVariants = new String[] { "en", "fr" };

	protected void setBundle(ConfigurationBundle bundle) {
		this.bundle = bundle;
	}

	@Test
	public void testHasValue() {
		assertFalse(bundle.hasValue("string1", noVariants));
		assertTrue(bundle.hasValue("string1", enVariants));

		assertFalse(bundle.hasValue("doesnotexist", noVariants));
		assertFalse(bundle.hasValue("doesnotexist", enVariants));
	}

	@Test
	public void testGetStringNoVariants() {
		Optional<String> actual = bundle.getString("string1", noVariants);
		assertEquals(Optional.of("string"), actual);

		actual = bundle.getString("string2", noVariants);
		assertEquals(Optional.of("string"), actual);

		actual = bundle.getString("string3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetStringEN() {
		Optional<String> actual = bundle.getString("string1", enVariants);
		assertEquals(Optional.of("string en"), actual);

		actual = bundle.getString("string2", enVariants);
		assertEquals(Optional.of("string en"), actual);

		actual = bundle.getString("string3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetIntegerNoVariants() {
		Optional<Integer> actual = bundle.getInteger("integer1", noVariants);
		assertEquals(Optional.of(1), actual);

		actual = bundle.getInteger("integer2", noVariants);
		assertEquals(Optional.of(1), actual);

		actual = bundle.getInteger("integer3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetIntegerEN() {
		Optional<Integer> actual = bundle.getInteger("integer1", enVariants);
		assertEquals(Optional.of(2), actual);

		actual = bundle.getInteger("integer2", enVariants);
		assertEquals(Optional.of(2), actual);

		actual = bundle.getInteger("integer3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetLongNoVariants() {
		Optional<Long> actual = bundle.getLong("integer1", noVariants);
		assertEquals(Optional.of(1L), actual);

		actual = bundle.getLong("integer2", noVariants);
		assertEquals(Optional.of(1L), actual);

		actual = bundle.getLong("integer3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetLongEN() {
		Optional<Long> actual = bundle.getLong("integer1", enVariants);
		assertEquals(Optional.of(2L), actual);

		actual = bundle.getLong("integer2", enVariants);
		assertEquals(Optional.of(2L), actual);

		actual = bundle.getLong("integer3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetDoubleNoVariants() {
		Optional<Double> actual = bundle.getDouble("number1", noVariants);
		assertEquals(Optional.of(1.2d), actual);

		actual = bundle.getDouble("number2", noVariants);
		assertEquals(Optional.of(1.2d), actual);

		actual = bundle.getDouble("number3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetDoubleEN() {
		Optional<Double> actual = bundle.getDouble("number1", enVariants);
		assertEquals(Optional.of(3.4d), actual);

		actual = bundle.getDouble("number2", enVariants);
		assertEquals(Optional.of(3.4d), actual);

		actual = bundle.getDouble("number3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetBooleanNoVariants() {
		Optional<Boolean> actual = bundle.getBoolean("boolean1", noVariants);
		assertEquals(Optional.of(true), actual);

		actual = bundle.getBoolean("boolean2", noVariants);
		assertEquals(Optional.of(true), actual);

		actual = bundle.getBoolean("boolean3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testGetBooleanEN() {
		Optional<Boolean> actual = bundle.getBoolean("boolean1", enVariants);
		assertEquals(Optional.of(false), actual);

		actual = bundle.getBoolean("boolean2", enVariants);
		assertEquals(Optional.of(false), actual);

		actual = bundle.getBoolean("boolean3", noVariants);
		assertEquals(Optional.absent(), actual);
	}

	@Test
	public void testOverride() {
		Optional<String> actual = bundle.getString("override", enVariants);
		assertEquals(Optional.of("value 2"), actual);
	}
}
