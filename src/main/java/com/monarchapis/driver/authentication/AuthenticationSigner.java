package com.monarchapis.driver.authentication;

import com.google.api.client.http.HttpRequest;

public interface AuthenticationSigner {
	public void signRequest(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken);
}
