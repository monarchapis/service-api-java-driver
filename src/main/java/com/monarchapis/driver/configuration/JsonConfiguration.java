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
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

/**
 * A configuration loaded by a JSON-formatted source.
 * 
 * @author Phil Kedy
 */
public class JsonConfiguration implements LoadableConfiguration {
	private JsonNode root;

	@Override
	public void load(byte[] content) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(content);
	}

	@Override
	public void load(File file) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(file);
	}

	@Override
	public void load(InputStream is) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(is);
	}

	@Override
	public void load(Reader reader) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(reader);
	}

	@Override
	public void load(String content) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(content);
	}

	@Override
	public void load(URL source) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(source);
	}

	/**
	 * Returns a Jackson object mapper for reading JSON-formatted input.
	 * 
	 * @return the object mapper.
	 */
	protected ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

	@Override
	public boolean hasValue(String path) {
		JsonNode node = getPathNode(path);

		return node.isValueNode();
	}

	@Override
	public Optional<String> getString(String path, Object... args) {
		JsonNode node = getPathNode(path);

		if (node.isTextual()) {
			String value = node.asText();

			if (args.length > 0) {
				return Optional.of(MessageFormat.format(value, args));
			} else {
				return Optional.of(value);
			}
		} else {
			return Optional.absent();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<Integer> getInteger(String path) {
		JsonNode node = getPathNode(path);

		return (Optional<Integer>) (!node.isInt() ? Optional.absent() : Optional.of(node.asInt()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<Long> getLong(String path) {
		JsonNode node = getPathNode(path);

		if (node.isInt()) {
			return Optional.of((long) node.asInt());
		}

		return (Optional<Long>) (!node.isLong() ? Optional.absent() : Optional.of(node.asLong()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<Boolean> getBoolean(String path) {
		JsonNode node = getPathNode(path);

		return (Optional<Boolean>) (!node.isBoolean() ? Optional.absent() : Optional.of(node.asBoolean()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Optional<Double> getDouble(String path) {
		JsonNode node = getPathNode(path);

		return (Optional<Double>) (!node.isDouble() ? Optional.absent() : Optional.of(node.asDouble()));
	}

	/**
	 * Returns a JSON node object for a given path.
	 * 
	 * @param path
	 *            The property path
	 * @return the JSON node.
	 */
	protected JsonNode getPathNode(String path) {
		String[] parts = StringUtils.split(path, '.');
		JsonNode node = root;

		for (int i = 0; i < parts.length; i++) {
			node = node.path(parts[i]);
		}

		return node;
	}
}
