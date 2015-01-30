package com.monarchapis.driver.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ApiResponse extends HttpServletResponseWrapper {
	protected HttpServletResponse response = null;
	protected OutputStream servletOutputStream = null;
	protected ServletOutputStreamWrapper stream = null;
	protected PrintWriter writer = null;

	public ApiResponse(HttpServletResponse response) {
		super(response);
		this.response = response;
	}

	public ServletOutputStreamWrapper createOutputStream() throws IOException {
		try {
			servletOutputStream = response.getOutputStream();
			return new ServletOutputStreamWrapper(servletOutputStream);
		} catch (Exception ex) {
			throw new IOException("Unable to construct servlet output stream: " + ex.getMessage(), ex);
		}
	}

	public void finishResponse() {
		try {
			if (writer != null) {
				writer.close();
			} else if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		stream.flush();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}

		if (stream == null) {
			stream = createOutputStream();
		}

		return stream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return (writer);
		}

		if (stream != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}

		stream = createOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(stream, response.getCharacterEncoding()));

		return (writer);
	}

	public OutputStream getServletOutputStream() {
		return servletOutputStream;
	}

	public int getDataSize() {
		int size = 0;

		try {
			// Servlet 3.0 only
			Collection<String> headerNames = response.getHeaderNames();

			for (String headerName : headerNames) {
				Collection<String> headerValues = getHeaders(headerName);

				for (String headerValue : headerValues) {
					size += headerName.length() + 2; // + ": "
					size += headerValue.length() + 1; // + \n
				}
			}
		} catch (Throwable mnfe) {
			// catch this for servlet version < 3.0
		}

		if (stream != null) {
			size += stream.getDataSize() + 1; // + \n for blank line
		}

		return size;
	}
}