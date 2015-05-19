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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ServletOutputStreamWrapperTest {
	@Test
	public void test() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ServletOutputStreamWrapper wrapper = new ServletOutputStreamWrapper(baos);

		assertTrue(wrapper.isReady());

		wrapper.write("This is a test".getBytes());
		wrapper.write('!');
		wrapper.flush();

		assertEquals(15, wrapper.getDataSize());

		wrapper.setWriteListener(null);

		wrapper.close();

		try {
			wrapper.write("This is a test".getBytes());
			fail("This should have thrown an exception");
		} catch (IOException ioe) {
			assertEquals("Cannot write to a closed output stream", ioe.getMessage());
		}

		try {
			wrapper.write('!');
			fail("This should have thrown an exception");
		} catch (IOException ioe) {
			assertEquals("Cannot write to a closed output stream", ioe.getMessage());
		}

		try {
			wrapper.flush();
			fail("This should have thrown an exception");
		} catch (IOException ioe) {
			assertEquals("Cannot flush a closed output stream", ioe.getMessage());
		}

		try {
			wrapper.close();
			fail("This should have thrown an exception");
		} catch (IOException ioe) {
			assertEquals("This output stream has already been closed", ioe.getMessage());
		}
	}
}
