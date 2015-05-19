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

package com.monarchapis.driver.model;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Optional;

/**
 * A wrapper to facilitate getting typed values from an abstract JSON object.
 * 
 * @author Phil Kedy
 */
public class Claims implements ClaimNames {
	private static ObjectMapper MAPPER;

	private ObjectNode data;

	static {
		MAPPER = new ObjectMapper();

		MAPPER.registerModule(new JodaModule());
		MAPPER.registerModule(new GuavaModule());

		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	public static Claims current() {
		return ClaimsHolder.getCurrent();
	}

	public Claims() {
		this.data = MAPPER.createObjectNode();
	}

	public Claims(ObjectNode root) {
		this.data = root;
	}

	public String getId() {
		return getString(ClaimNames.ID).orNull();
	}

	public Optional<String> getSubject() {
		return getString(ClaimNames.SUBJECT);
	}

	public boolean hasClaim(String... path) {
		JsonNode node = getPathNode(path);
		return !node.isMissingNode();
	}

	public boolean hasClientPermission(String... permissions) {
		return hasPermission(CLIENT, permissions);
	}

	public boolean hasTokenPermission(String... permissions) {
		return hasPermission(TOKEN, permissions);
	}

	private boolean hasPermission(String claimName, String... permissions) {
		if (permissions == null || permissions.length == 0) {
			return true;
		}

		Optional<Authorizable> _authorizable = this.getAs(Authorizable.class, claimName);

		if (_authorizable.isPresent()) {
			Authorizable authorizable = _authorizable.get();
			Set<String> authorized = authorizable.getPermissions();

			if (authorized != null) {
				for (String permission : permissions) {
					if (authorized.contains(permission)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public Optional<String> getString(String... path) {
		JsonNode node = getPathNode(path);

		if (node.isTextual()) {
			return Optional.of(node.asText());
		} else {
			return Optional.absent();
		}
	}

	public boolean hasValueInClaim(String value, String... path) {
		Validate.notEmpty(value, "value is a required parameter");
		JsonNode node = getPathNode(path);

		if (node.isTextual()) {
			return value.equals(node.asText());
		} else if (node.isArray()) {
			ArrayNode array = (ArrayNode) node;
			for (Iterator<JsonNode> iter = array.iterator(); iter.hasNext();) {
				JsonNode item = iter.next();

				if (item.isTextual() && value.equals(item.asText())) {
					return true;
				}
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public Optional<Integer> getInteger(String... path) {

		JsonNode node = getPathNode(path);

		return (Optional<Integer>) (!node.isInt() ? Optional.absent() : Optional.of(node.asInt()));
	}

	@SuppressWarnings("unchecked")
	public Optional<Long> getLong(String... path) {
		JsonNode node = getPathNode(path);

		if (node.isInt()) {
			return Optional.of((long) node.asInt());
		}

		return (Optional<Long>) (!node.isLong() ? Optional.absent() : Optional.of(node.asLong()));
	}

	@SuppressWarnings("unchecked")
	public Optional<Boolean> getBoolean(String... path) {
		JsonNode node = getPathNode(path);

		return (Optional<Boolean>) (!node.isBoolean() ? Optional.absent() : Optional.of(node.asBoolean()));
	}

	@SuppressWarnings("unchecked")
	public Optional<Double> getDouble(String... path) {
		JsonNode node = getPathNode(path);

		return (Optional<Double>) (!node.isDouble() ? Optional.absent() : Optional.of(node.asDouble()));
	}

	public <T> Optional<T> getAs(Class<T> clazz, String... path) {
		JsonNode node = getPathNode(path);

		if (node.isObject()) {
			return Optional.of(MAPPER.convertValue(node, clazz));
		} else {
			return Optional.absent();
		}
	}

	/**
	 * Returns a JSON node object for a given path.
	 * 
	 * @param path
	 *            The property path
	 * @return the JSON node.
	 */
	protected JsonNode getPathNode(String... path) {
		Validate.isTrue(path.length > 0, "path requires at least one key");
		JsonNode node = data;

		for (int i = 0; i < path.length; i++) {
			node = node.path(path[i]);
		}

		return node;
	}

	public ObjectNode getData() {
		return data;
	}

	public void setData(ObjectNode data) {
		this.data = data;
	}
}
