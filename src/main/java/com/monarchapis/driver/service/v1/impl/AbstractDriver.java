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

package com.monarchapis.driver.service.v1.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.monarchapis.driver.authentication.AuthenticationSigner;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.model.ProviderCredentials;

public class AbstractDriver {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDriver.class);

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	protected static String GET = "GET";
	protected static String POST = "POST";
	protected static String PUT = "PUT";
	protected static String PATCH = "PATCH";
	protected static String DELETE = "DELETE";

	private HttpRequestFactory requestFactory;

	private String baseUrl;

	@Inject
	private ProviderCredentials providerCredentials;

	@Inject
	private AuthenticationSigner authenticationSigner;

	public AbstractDriver() {
		requestFactory = HTTP_TRANSPORT.createRequestFactory();
	}

	protected HttpRequestFactory getRequestFactory() {
		return requestFactory;
	}

	protected <T> T executeJsonRequest(String method, String url, Class<T> clazz) {
		HttpResponse response = executeJsonRequest(method, url);
		return parseAs(response, clazz);
	}

	protected <T> T executeJsonRequest(String method, GenericUrl url, Class<T> clazz) {
		HttpResponse response = executeJsonRequest(method, url);
		return parseAs(response, clazz);
	}

	protected <T> T executeJsonRequest(String method, String url, TypeReference<T> reference) {
		HttpResponse response = executeJsonRequest(method, url);
		return parseAs(response, reference);
	}

	protected <T> T executeJsonRequest(String method, GenericUrl url, TypeReference<T> reference) {
		HttpResponse response = executeJsonRequest(method, url);
		return parseAs(response, reference);
	}

	protected <T> T executeJsonRequest(String method, String url, Object obj, Class<T> clazz) {
		HttpResponse response = executeJsonRequest(method, url, obj);
		return parseAs(response, clazz);
	}

	protected <T> T executeJsonRequest(String method, GenericUrl url, Object obj, Class<T> clazz) {
		HttpResponse response = executeJsonRequest(method, url, obj);
		return parseAs(response, clazz);
	}

	protected <T> T executeJsonRequest(String method, String url, Object obj, TypeReference<T> reference) {
		HttpResponse response = executeJsonRequest(method, url, obj);
		return parseAs(response, reference);
	}

	protected <T> T executeJsonRequest(String method, GenericUrl url, Object obj, TypeReference<T> reference) {
		HttpResponse response = executeJsonRequest(method, url, obj);
		return parseAs(response, reference);
	}

	protected HttpResponse executeJsonRequest(String method, String url) {
		GenericUrl genericUrl = createUrl(url);
		return executeJsonRequest(method, genericUrl);
	}

	protected HttpResponse executeJsonRequest(String method, String url, Object obj) {
		GenericUrl genericUrl = createUrl(url);
		return executeJsonRequest(method, genericUrl, obj);
	}

	protected HttpResponse executeJsonRequest(String method, GenericUrl url) {
		try {
			HttpRequest request = createRequest(method, url);

			return request.execute();
		} catch (HttpResponseException hre) {
			throw processApiError(hre);
		} catch (IOException e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected HttpResponse executeJsonRequest(String method, GenericUrl url, Object obj) {
		try {
			HttpRequest request = createRequest(method, url, obj);

			return request.execute();
		} catch (HttpResponseException hre) {
			throw processApiError(hre);
		} catch (IOException e) {
			throw new ApiException("Error processing request", e);
		}
	}

	protected HttpResponse executeRequest(String method, GenericUrl url) {
		try {
			HttpRequest request = createRequest(method, url);

			return request.execute();
		} catch (HttpResponseException hre) {
			throw processApiError(hre);
		} catch (IOException e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected HttpRequest createRequest(String method, GenericUrl url) throws IOException {
		HttpRequest request = requestFactory.buildRequest(method, url, null);
		setRequestHeaders(request);

		return request;
	}

	protected HttpRequest createRequest(String method, GenericUrl url, Object obj) throws IOException {
		HttpContent content = getHttpContent(obj);
		HttpRequest request = requestFactory.buildRequest(method, url, content);
		setRequestHeaders(request);

		return request;
	}

	protected GenericUrl createUrl(String url) {
		return new GenericUrl(baseUrl + url);
	}

	protected void setRequestHeaders(HttpRequest request) {
		HttpHeaders headers = request.getHeaders();
		setMimeTypes(request, headers);
		setCustomHeaders(request, headers);
		signRequest(request);
	}

	private void setMimeTypes(HttpRequest request, HttpHeaders headers) {
		headers.setAccept("application/json");
		headers.setContentEncoding("UTF-8");
	}

	protected void setCustomHeaders(HttpRequest request, HttpHeaders headers) {
	}

	private void signRequest(HttpRequest request) {
		if (authenticationSigner != null && providerCredentials != null) {
			authenticationSigner.signRequest(request, "sha256", providerCredentials.getApiKey(),
					providerCredentials.getSharedSecret(), null);
		}
	}

	protected static HttpContent getHttpContent(Object o) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			byte[] data = mapper.writeValueAsBytes(o);
			return new ByteArrayContent("application/json", data);
		} catch (JsonProcessingException e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected static <T> T parseAs(HttpResponse response, Class<T> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(response.getContent(), clazz);
		} catch (Exception e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected static <T> T parseAs(HttpResponse response, TypeReference<T> reference) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(response.getContent(), reference);
		} catch (Exception e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected static String encodeUriComponent(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Could not encode URI component", e);
		}
	}

	private static ApiException processApiError(HttpResponseException hre) {
		ApiError error = parseApiError(hre);

		return (error != null) ? new ApiErrorException(error) : new ApiException("Could not process error response");
	}

	private static ApiError parseApiError(HttpResponseException hre) {
		ApiError error = null;
		String content = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			content = hre.getContent();

			if (content != null) {
				error = mapper.readValue(content, ApiError.class);
			}
		} catch (IOException ioe) {
			// Ignore and fall through
			logger.error("Could not read error response: {}", content);
		}

		return error;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		Validate.notNull(baseUrl, "baseUrl is required.");
		this.baseUrl = baseUrl;
	}

	public ProviderCredentials getProviderCredentials() {
		return providerCredentials;
	}

	public void setProviderCredentials(ProviderCredentials providerCredentials) {
		Validate.notNull(providerCredentials, "providerCredentials is required.");
		this.providerCredentials = providerCredentials;
	}

	public AuthenticationSigner getAuthenticationSigner() {
		return authenticationSigner;
	}

	public void setAuthenticationSigner(AuthenticationSigner authenticationSigner) {
		this.authenticationSigner = authenticationSigner;
	}
}
