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
