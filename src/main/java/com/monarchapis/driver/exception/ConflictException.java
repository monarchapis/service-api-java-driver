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

package com.monarchapis.driver.exception;

/**
 * Represents a conflict exception with an optional template and arguments for
 * messaging formatting.
 * 
 * @author Phil Kedy
 */
public class ConflictException extends ApiErrorException {
	private static final long serialVersionUID = 7343066763779729612L;

	public ConflictException() {
		super("conflict");
	}

	public ConflictException(Throwable t) {
		super("conflict", t);
	}

	public ConflictException(String template, String... args) {
		super("conflict", template, args);
	}

	public ConflictException(Throwable t, String template, String... args) {
		super("conflict", t, template, args);
	}
}