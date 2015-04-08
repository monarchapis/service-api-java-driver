package com.monarchapis.driver.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseTest {
	private ByteArrayOutputStream os;

	@Mock
	private HttpServletResponse response;

	private ApiResponse apiResponse;

	@Before
	public void setup() throws IOException {
		os = new ByteArrayOutputStream();

		when(response.getOutputStream()).thenAnswer(new Answer<ServletOutputStream>() {
			@Override
			public ServletOutputStream answer(InvocationOnMock invocation) throws Throwable {
				return new ServletOutputStreamWrapper(os);
			}
		});
		when(response.getCharacterEncoding()).thenReturn("UTF8");
		when(response.getHeaderNames()).thenReturn(Lists.newArrayList("name1", "name2"));
		when(response.getHeaders("name1")).thenReturn(Lists.newArrayList("name1-value1", "name1-value2"));
		when(response.getHeaders("name2")).thenReturn(Lists.newArrayList("name2-value1", "name2-value2"));

		apiResponse = new ApiResponse(response);
	}

	@Test
	public void testGetWriter() throws IOException {
		PrintWriter writer = apiResponse.getWriter();
		assertSame(writer, apiResponse.getWriter());

		String expected = "THIS IS A TEST";
		writer.print(expected);
		apiResponse.flushBuffer();
		String actual = os.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testGetServletOutputStream() throws IOException {
		ServletOutputStream outputStream = apiResponse.getOutputStream();
		String expected = "THIS IS A TEST";
		outputStream.write(expected.getBytes());
		apiResponse.flushBuffer();
		String actual = os.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testOutputStreamBeforeWriter() throws IOException {
		apiResponse.getOutputStream();

		try {
			apiResponse.getWriter();
			fail("This should have thrown an exception");
		} catch (IllegalStateException ise) {
			assertEquals("getOutputStream() has already been called!", ise.getMessage());
		}
	}

	@Test
	public void testWriterBeforeOutputStream() throws IOException {
		apiResponse.getWriter();

		try {
			apiResponse.getOutputStream();
			fail("This should have thrown an exception");
		} catch (IllegalStateException ise) {
			assertEquals("getWriter() has already been called!", ise.getMessage());
		}
	}

	@Test
	public void testGetDataSize() throws IOException {
		PrintWriter writer = apiResponse.getWriter();
		writer.print("THIS IS A TEST");
		apiResponse.flushBuffer();
		assertEquals(95, apiResponse.getDataSize());
	}

	@Test
	public void testCreateOutputStreamException() throws IOException {
		when(response.getOutputStream()).thenThrow(new RuntimeException("test"));

		try {
			apiResponse.getWriter();
		} catch (IOException ioe) {
			assertEquals("Unable to construct servlet output stream: test", ioe.getMessage());
		}
	}
}
