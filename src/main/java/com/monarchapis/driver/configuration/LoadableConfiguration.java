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

package com.monarchapis.driver.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * Adds methods to load a configuration from various inputs.
 * 
 * @author Phil Kedy
 */
public interface LoadableConfiguration extends Configuration {
	/**
	 * Loads the configuration via a byte array.
	 * 
	 * @param content
	 *            The byte array
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(byte[] content) throws IOException;

	/**
	 * Loads the configuration from a file.
	 * 
	 * @param file
	 *            The input file to read
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(File file) throws IOException;

	/**
	 * Loads a configuration via an input stream.
	 * 
	 * @param is
	 *            The input stream
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(InputStream is) throws IOException;

	/**
	 * Loads a configuration via a reader.
	 * 
	 * @param reader
	 *            The reader instance
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(Reader reader) throws IOException;

	/**
	 * Load the configuration via a string.
	 * 
	 * @param content
	 *            The string content to read.
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(String content) throws IOException;

	/**
	 * Loads a configuration via a URL.
	 * 
	 * @param source
	 *            The URL to read.
	 * @throws IOException
	 *             when the configuration cannot be read.
	 */
	public void load(URL source) throws IOException;
}
