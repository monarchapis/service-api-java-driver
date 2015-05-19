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

package com.monarchapis.driver.servlet;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ServletInputStreamWrapperTest {
	private ServletInputStreamWrapper wrapper;

	@Before
	public void setup() {
		wrapper = new ServletInputStreamWrapper("test".getBytes());
	}

	@Test
	public void testRead() {
		assertTrue(wrapper.isReady());
		assertFalse(wrapper.isFinished());
		assertEquals('t', wrapper.read());
		assertTrue(wrapper.isReady());
		assertFalse(wrapper.isFinished());
		assertEquals('e', wrapper.read());
		assertTrue(wrapper.isReady());
		assertFalse(wrapper.isFinished());
		assertEquals('s', wrapper.read());
		assertTrue(wrapper.isReady());
		assertFalse(wrapper.isFinished());
		assertEquals('t', wrapper.read());
		assertFalse(wrapper.isReady());
		assertTrue(wrapper.isFinished());
		assertEquals(-1, wrapper.read());
	}
}
