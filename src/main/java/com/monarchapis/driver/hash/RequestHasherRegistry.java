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

package com.monarchapis.driver.hash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.monarchapis.driver.exception.ApiException;

/**
 * Provides a lookup mechanism for retrieving request hashers by name.
 * 
 * @author Phil Kedy
 */
public class RequestHasherRegistry {
	/**
	 * The provided list of request hashers.
	 */
	private List<RequestHasher> hashers;

	/**
	 * The request hasher dictionary lookup where the request hasher name is
	 * used as the key.
	 */
	private Map<String, RequestHasher> lookup;

	public RequestHasherRegistry() {
		lookup = new HashMap<String, RequestHasher>();
	}

	public RequestHasherRegistry(List<RequestHasher> hashers) {
		setRequestHashers(hashers);
	}

	public RequestHasherRegistry(RequestHasher... hashers) {
		setRequestHashers(Lists.newArrayList(hashers));
	}

	/**
	 * Initializes the dictionary lookup using a list of request hashers.
	 * 
	 * @param hashers
	 *            The request hashers
	 */
	@Inject
	public void setRequestHashers(List<RequestHasher> hashers) {
		Map<String, RequestHasher> lookup = new HashMap<String, RequestHasher>();

		for (RequestHasher hasher : hashers) {
			lookup.put(hasher.getName(), hasher);
		}

		this.lookup = lookup;
		this.hashers = Collections.unmodifiableList(new ArrayList<RequestHasher>(hashers));
	}

	/**
	 * Retrieves a request hasher by name.
	 * 
	 * @param name
	 *            The desired request hasher name
	 * @return the request hasher if found.
	 * @throws ApiException
	 *             when the request hasher is not found.
	 */
	public RequestHasher getRequestHasher(String name) {
		RequestHasher ret = lookup.get(name);

		if (ret == null) {
			throw new ApiException("Request hasher " + name + " is not registered");
		}

		return ret;
	}

	public List<RequestHasher> getRequestHashers() {
		return hashers;
	}
}
