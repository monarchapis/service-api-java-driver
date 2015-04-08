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

public class JsonConfiguration implements LoadableConfiguration {
	private JsonNode root;

	public void load(byte[] content) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(content);
	}

	public void load(File file) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(file);
	}

	public void load(InputStream is) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(is);
	}

	public void load(Reader reader) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(reader);
	}

	public void load(String content) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(content);
	}

	public void load(URL source) throws IOException {
		ObjectMapper mapper = getObjectMapper();
		root = mapper.readTree(source);
	}

	protected ObjectMapper getObjectMapper() {
		return new ObjectMapper();
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

	protected JsonNode getPathNode(String path) {
		String[] parts = StringUtils.split(path, '.');
		JsonNode node = root;

		for (int i = 0; i < parts.length; i++) {
			node = node.path(parts[i]);
		}

		return node;
	}
}
