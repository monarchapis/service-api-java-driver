package com.monarchapis.driver.service.v1.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import com.monarchapis.driver.authentication.ProviderCredentials;
import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.util.ErrorUtils;

public class AbstractDriver {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AbstractDriver.class);

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	protected static String GET = "GET";
	protected static String POST = "POST";
	protected static String PUT = "PUT";
	protected static String PATCH = "PATCH";
	protected static String DELETE = "DELETE";

	private String baseUrl;
	private ProviderCredentials providerCredentials;

	protected HttpRequestFactory requestFactory;

	private AuthenticationSigner authenticationSigner;

	public AbstractDriver(String baseUrl, ProviderCredentials providerCredentials) {
		this(baseUrl, providerCredentials, null);
	}

	public AbstractDriver(String baseUrl, ProviderCredentials providerCredentials,
			AuthenticationSigner authenticationSigner) {
		if (baseUrl == null) {
			throw new IllegalArgumentException("baseUrl is required.");
		}

		this.baseUrl = baseUrl;
		this.providerCredentials = providerCredentials;
		this.authenticationSigner = authenticationSigner;

		requestFactory = HTTP_TRANSPORT.createRequestFactory();
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
			throw ErrorUtils.processApiError(hre);
		} catch (IOException e) {
			throw new ApiException("Error processing authorization", e);
		}
	}

	protected HttpResponse executeJsonRequest(String method, GenericUrl url, Object obj) {
		try {
			HttpRequest request = createRequest(method, url, obj);

			return request.execute();
		} catch (HttpResponseException hre) {
			throw ErrorUtils.processApiError(hre);
		} catch (IOException e) {
			throw new ApiException("Error processing request", e);
		}
	}

	protected HttpResponse executeRequest(String method, GenericUrl url) {
		try {
			HttpRequest request = createRequest(method, url);

			return request.execute();
		} catch (HttpResponseException hre) {
			throw ErrorUtils.processApiError(hre);
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

	public AuthenticationSigner getAuthenticationSigner() {
		return authenticationSigner;
	}

	public void setAuthenticationSigner(AuthenticationSigner authenticationSigner) {
		this.authenticationSigner = authenticationSigner;
	}

	protected static class GetIdResponse {
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}
}
