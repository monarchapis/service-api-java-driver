package com.monarchapis.driver.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageUtilsTest {
	@Test
	public void testFormat() {
		String actual = MessageUtils.format("{} = {}", "value 1", "value 2", "value 3");
		String expected = "value 1 = value 2";
		assertEquals(expected, actual);
	}
}
