package com.monarchapis.driver.hash;

import static org.junit.Assert.*;

import org.junit.Test;

import com.monarchapis.driver.exception.ApiException;

public class HasherUtilsTest {
	@Test
	public void testGetHMacAlgorithm() {
		assertEquals("MD5", HasherUtils.getMessageDigestAlgorithm("md5"));
		assertEquals("SHA-1", HasherUtils.getMessageDigestAlgorithm("sha1"));
		assertEquals("SHA-256", HasherUtils.getMessageDigestAlgorithm("sha256"));
		assertEquals("SHA-384", HasherUtils.getMessageDigestAlgorithm("sha384"));
		assertEquals("SHA-512", HasherUtils.getMessageDigestAlgorithm("sha512"));

		try {
			HasherUtils.getMessageDigestAlgorithm("does not exist");
			fail("An exception should have been thrown");
		} catch (ApiException apie) {
			assertEquals("Invalid algorithm does not exist", apie.getMessage());
		}
	}
}
