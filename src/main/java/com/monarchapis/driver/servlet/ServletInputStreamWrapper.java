package com.monarchapis.driver.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class ServletInputStreamWrapper extends ServletInputStream {
	private byte[] data;
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
