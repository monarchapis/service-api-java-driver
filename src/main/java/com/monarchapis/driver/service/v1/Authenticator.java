package com.monarchapis.driver.service.v1;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.monarchapis.driver.hash.RequestHasher;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.HasherAlgorithm;
import com.monarchapis.driver.model.HttpHeader;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.servlet.ApiRequest;

public class Authenticator {
	public static AuthenticationResponse processAuthentication(BigDecimal requestWeight) {
		ServiceContainer serviceContainer = ServiceContainer.getInstance();
		ServiceApi serviceApi = serviceContainer.getServiceApi();
		ApiRequest apiRequest = ApiRequest.getCurrent();
		HttpServletResponse httpResponse = HttpResponseHolder.getCurrent();

		AuthenticationRequest authRequest = prepareAuthenticationRequest(serviceContainer, apiRequest);
		authRequest.setRequestWeight(requestWeight);
		AuthenticationResponse authResponse = serviceApi.authenticate(authRequest);

		if (authResponse.getResponseHeaders() != null) {
			for (HttpHeader header : authResponse.getResponseHeaders()) {
				httpResponse.addHeader(header.getName(), header.getValue());
			}
		}

		return authResponse;
	}

	private static AuthenticationRequest prepareAuthenticationRequest(ServiceContainer serviceContainer,
			ApiRequest apiRequest) {
		AuthenticationRequest authRequest = apiRequest.createAuthorizationRequest();
		List<HasherAlgorithm> hasherAlgorithms = serviceContainer.getHasherAlgorithms();

		for (HasherAlgorithm hasherAlgo : hasherAlgorithms) {
			RequestHasher hasher = serviceContainer.getRequestHasherRegistry().getRequestHasher(hasherAlgo.getName());

			for (String algorithm : hasherAlgo.getAlgorithms()) {
				String hash = hasher.getRequestHash(apiRequest, algorithm);
				authRequest.setPayloadHash(hasherAlgo.getName(), algorithm, hash);
			}
		}

		return authRequest;
	}
}
