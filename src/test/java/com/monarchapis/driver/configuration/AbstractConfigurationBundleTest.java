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
}
