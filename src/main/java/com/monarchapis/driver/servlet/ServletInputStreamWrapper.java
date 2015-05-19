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

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * An implementation of ServletInputStream that wraps a byte array.
 * 
 * @author Phil Kedy
 */
public class ServletInputStreamWrapper extends ServletInputStream {
	/**
	 * The byte array data to stream.
	 */
	private byte[] data;

	/**
	 * The current stream index.
	 */
	private int idx = 0;

	public ServletInputStreamWrapper(byte[] data) {
		this.data = data;
	}

	@Override
	public int read() {
		return isFinished() ? -1 : data[idx++] & 0xff;
	}

	@Override
	public boolean isFinished() {
		return idx >= data.length;
	}

	@Override
	public boolean isReady() {
		return idx < data.length;
	}

	@Override
	public void setReadListener(ReadListener listener) {
	}
}
