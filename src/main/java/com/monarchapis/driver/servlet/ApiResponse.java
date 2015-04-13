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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrappers a HttpServletResponse and provides additional methods to support
 * various authentication schemes such as Hawk.
 * 
 * @author Phil Kedy
 */
public class ApiResponse extends HttpServletResponseWrapper {
	/**
	 * The underlying response.
	 */
	protected HttpServletResponse response = null;

	/**
	 * The servlet response output stream.
	 */
	protected ServletOutputStreamWrapper stream = null;

	/**
	 * The print writer if getWriter() is called.
	 */
	protected PrintWriter writer = null;

	public ApiResponse(HttpServletResponse response) {
		super(response);
		this.response = response;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (stream != null) {
			stream.flush();
		}

		if (writer != null) {
			writer.flush();
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
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

	/**
	 * Calculates the data size of the response.
	 * 
	 * @return the response data size.
	 */
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

	/**
	 * Creates the underlying ServletOutputStreamWrapper.
	 * 
	 * @return the ServletOutputStreamWrapper.
	 * @throws IOException
	 *             when the wrapper could not be create.
	 */
	private ServletOutputStreamWrapper createOutputStream() throws IOException {
		try {
			OutputStream servletOutputStream = response.getOutputStream();

			return new ServletOutputStreamWrapper(servletOutputStream);
		} catch (Exception ex) {
			throw new IOException("Unable to construct servlet output stream: " + ex.getMessage(), ex);
		}
	}
}