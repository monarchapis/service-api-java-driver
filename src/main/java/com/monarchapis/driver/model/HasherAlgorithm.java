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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a hasher algorithm that should be processed on the API request as
 * part of the request authentication call.
 * 
 * @author Phil Kedy
 */
public class HasherAlgorithm {
	/**
	 * The hasher algorithm name.
	 */
	private String name;

	/**
	 * The security algorithms to execute on the request body. (e.g. md5, sha1,
	 * sha256, sha512)
	 */
	private String[] algorithms;

	public HasherAlgorithm(String name, String[] algorithms) {
		this.name = name;
		this.algorithms = algorithms;
	}

	public String getName() {
		return name;
	}

	public String[] getAlgorithms() {
		return algorithms;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}