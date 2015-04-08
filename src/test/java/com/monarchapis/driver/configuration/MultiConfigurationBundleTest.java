package com.monarchapis.driver.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MultiConfigurationBundleTest {
	@Mock
	private ConfigurationBundle config1;

	@Mock
	private ConfigurationBundle config2;

	private MultiConfigurationBundle bundle;

	private String[] variants;

	private String path;

	@Before
	public void setup() {
		bundle = new MultiConfigurationBundle(config1, config2);
		variants = new String[] { "var1", "var2" };
		path = "test.path";
	}

	@Test
	public void testGetBundles() {
		assertEquals( //
				Lists.newArrayList(new ConfigurationBundle[] { config1, config2 }), //
				Lists.newArrayList(bundle.getBundles()));
	}

	@Test
	public void testEmptyConstructor() {
		bundle = new MultiConfigurationBundle();
		assertEquals(0, bundle.getBundles().length);
	}

	@Test
	public void testStringNotFound() {
		when(config1.getString(path, variants)).thenReturn(absent(String.class));
		when(config2.getString(path, variants)).thenReturn(absent(String.class));
		Optional<String> actual = bundle.getString(path, variants);
		assertEquals(Optional.absent(), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getString(path, variants);
		verify(config2, times(1)).getString(path, variants);
	}

	@Test
	public void testDefaultStringInFirst() {
		when(config1.getString(path, variants)).thenReturn(Optional.of("from1"));
		when(config2.getString(path, variants)).thenReturn(absent(String.class));
		Optional<String> actual = bundle.getString(path, variants);
		assertEquals(Optional.of("from1"), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getString(path, variants);
		verify(config2, times(1)).getString(path, variants);
	}

	@Test
	public void testDefaultStringInLast() {
		when(config1.getString(path, variants)).thenReturn(absent(String.class));
		when(config2.getString(path, variants)).thenReturn(Optional.of("from2"));
		Optional<String> actual = bundle.getString(path, variants);
		assertEquals(Optional.of("from2"), actual);
		// The search index goes from last to first so both are called
		verify(config1, never()).getString(path, variants);
		verify(config2, times(1)).getString(path, variants);
	}

	@Test
	public void testIntegerNotFound() {
		when(config1.getInteger(path, variants)).thenReturn(absent(Integer.class));
		when(config2.getInteger(path, variants)).thenReturn(absent(Integer.class));
		Optional<Integer> actual = bundle.getInteger(path, variants);
		assertEquals(Optional.absent(), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getInteger(path, variants);
		verify(config2, times(1)).getInteger(path, variants);
	}

	@Test
	public void testDefaultIntegerInFirst() {
		when(config1.getInteger(path, variants)).thenReturn(Optional.of(1));
		when(config2.getInteger(path, variants)).thenReturn(absent(Integer.class));
		Optional<Integer> actual = bundle.getInteger(path, variants);
		assertEquals(Optional.of(1), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getInteger(path, variants);
		verify(config2, times(1)).getInteger(path, variants);
	}

	@Test
	public void testDefaultIntegerInLast() {
		when(config1.getInteger(path, variants)).thenReturn(absent(Integer.class));
		when(config2.getInteger(path, variants)).thenReturn(Optional.of(2));
		Optional<Integer> actual = bundle.getInteger(path, variants);
		assertEquals(Optional.of(2), actual);
		// The search index goes from last to first so both are called
		verify(config1, never()).getInteger(path, variants);
		verify(config2, times(1)).getInteger(path, variants);
	}

	@Test
	public void testLongNotFound() {
		when(config1.getLong(path, variants)).thenReturn(absent(Long.class));
		when(config2.getLong(path, variants)).thenReturn(absent(Long.class));
		Optional<Long> actual = bundle.getLong(path, variants);
		assertEquals(Optional.absent(), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getLong(path, variants);
		verify(config2, times(1)).getLong(path, variants);
	}

	@Test
	public void testDefaultLongInFirst() {
		when(config1.getLong(path, variants)).thenReturn(Optional.of(1L));
		when(config2.getLong(path, variants)).thenReturn(absent(Long.class));
		Optional<Long> actual = bundle.getLong(path, variants);
		assertEquals(Optional.of(1L), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getLong(path, variants);
		verify(config2, times(1)).getLong(path, variants);
	}

	@Test
	public void testDefaultLongInLast() {
		when(config1.getLong(path, variants)).thenReturn(absent(Long.class));
		when(config2.getLong(path, variants)).thenReturn(Optional.of(2L));
		Optional<Long> actual = bundle.getLong(path, variants);
		assertEquals(Optional.of(2L), actual);
		// The search index goes from last to first so both are called
		verify(config1, never()).getLong(path, variants);
		verify(config2, times(1)).getLong(path, variants);
	}

	@Test
	public void testDoubleNotFound() {
		when(config1.getDouble(path, variants)).thenReturn(absent(Double.class));
		when(config2.getDouble(path, variants)).thenReturn(absent(Double.class));
		Optional<Double> actual = bundle.getDouble(path, variants);
		assertEquals(Optional.absent(), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getDouble(path, variants);
		verify(config2, times(1)).getDouble(path, variants);
	}

	@Test
	public void testDefaultDoubleInFirst() {
		when(config1.getDouble(path, variants)).thenReturn(Optional.of(1d));
		when(config2.getDouble(path, variants)).thenReturn(absent(Double.class));
		Optional<Double> actual = bundle.getDouble(path, variants);
		assertEquals(Optional.of(1d), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getDouble(path, variants);
		verify(config2, times(1)).getDouble(path, variants);
	}

	@Test
	public void testDefaultDoubleInLast() {
		when(config1.getDouble(path, variants)).thenReturn(absent(Double.class));
		when(config2.getDouble(path, variants)).thenReturn(Optional.of(2d));
		Optional<Double> actual = bundle.getDouble(path, variants);
		assertEquals(Optional.of(2d), actual);
		// The search index goes from last to first so both are called
		verify(config1, never()).getDouble(path, variants);
		verify(config2, times(1)).getDouble(path, variants);
	}

	@Test
	public void testBooleanNotFound() {
		when(config1.getBoolean(path, variants)).thenReturn(absent(Boolean.class));
		when(config2.getBoolean(path, variants)).thenReturn(absent(Boolean.class));
		Optional<Boolean> actual = bundle.getBoolean(path, variants);
		assertEquals(Optional.absent(), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getBoolean(path, variants);
		verify(config2, times(1)).getBoolean(path, variants);
	}

	@Test
	public void testDefaultBooleanInFirst() {
		when(config1.getBoolean(path, variants)).thenReturn(Optional.of(true));
		when(config2.getBoolean(path, variants)).thenReturn(absent(Boolean.class));
		Optional<Boolean> actual = bundle.getBoolean(path, variants);
		assertEquals(Optional.of(true), actual);
		// The search index goes from last to first so both are called
		verify(config1, times(1)).getBoolean(path, variants);
		verify(config2, times(1)).getBoolean(path, variants);
	}

	@Test
	public void testDefaultBooleanInLast() {
		when(config1.getBoolean(path, variants)).thenReturn(absent(Boolean.class));
		when(config2.getBoolean(path, variants)).thenReturn(Optional.of(false));
		Optional<Boolean> actual = bundle.getBoolean(path, variants);
		assertEquals(Optional.of(false), actual);
		// The search index goes from last to first so both are called
		verify(config1, never()).getBoolean(path, variants);
		verify(config2, times(1)).getBoolean(path, variants);
	}

	private static <T> Optional<T> absent(Class<T> clazz) {
		return Optional.absent();
	}
}
