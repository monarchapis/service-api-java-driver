package com.monarchapis.driver.hash;

import com.monarchapis.driver.servlet.ApiRequest;

public interface RequestHasher {
	public String getName();

	public String getRequestHash(ApiRequest request, String algorithm);
}
