package com.monarchapis.driver.servlet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

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
