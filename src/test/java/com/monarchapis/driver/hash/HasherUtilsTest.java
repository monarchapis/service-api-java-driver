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
