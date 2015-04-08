package com.monarchapis.driver.authentication;

import static org.junit.Assert.*;

import org.junit.Test;

import com.monarchapis.driver.exception.ApiException;

public class HmacUtilsTest {
	@Test
	public void testGetHMacAlgorithm() {
		assertEquals("HmacMD5", HmacUtils.getHMacAlgorithm("md5"));
		assertEquals("HmacSHA1", HmacUtils.getHMacAlgorithm("sha1"));
		assertEquals("HmacSHA256", HmacUtils.getHMacAlgorithm("sha256"));

		try {
			HmacUtils.getHMacAlgorithm("does not exist");
			fail("An exception should have been thrown");
		} catch (ApiException apie) {
			assertEquals("Invalid algorithm does not exist", apie.getMessage());
		}
	}
}
