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

package com.monarchapis.driver.spring.rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.exception.ApiErrorFactory;

@RunWith(MockitoJUnitRunner.class)
public class ApiErrorResponseEntityExceptionHandlerTest {
	@Mock
	private ApiErrorFactory apiErrorFactory;

	@Mock
	private HttpServletRequest request;

	@Mock
	private WebRequest webRequest;

	@InjectMocks
	private ApiErrorResponseEntityExceptionHandler hander;

	@Before
	public void setup() {
		when(request.getRequestURI()).thenReturn("/test");

		setError(404, "notFound");
		setError(400, "methodNotAllowed");
		setError(400, "mediaTypeNotSupported");
		setError(400, "mediaTypeNotAcceptable");
		setError(400, "missingParameter");
		setError(400, "bindingException");
		setError(400, "conversionNotSupported");
		setError(400, "typeMismatch");
		setError(400, "messageNotReadable");
		setError(400, "messageNotWritable");
		setError(400, "invalidParameter");
		setError(400, "missingRequestPart");
		setError(500, "internalError");
	}

	@Test
	public void testApiErrorException() {
		ApiError error = new ApiError(404, "reason", "message", "developerMessage", "errorCode");
		ResponseEntity<ApiError> response = hander.handleApiErrorException(request, new ApiErrorException(error));
		ApiError actual = response.getBody();
		assertEquals(404, response.getStatusCode().value());
		// assertEquals(error.getReason(), actual.getReason());
		assertSame(error, actual);
	}

	@Test
	public void testNoSuchRequestHandlingMethodException() {
		performTest(new NoSuchRequestHandlingMethodException(request), 404, "notFound");
	}

	@Test
	public void testHttpRequestMethodNotSupported() {
		Set<String> allowedMethods = Sets.newLinkedHashSet(Lists.newArrayList("GET", "POST"));
		ResponseEntity<Object> response = //
		performTest(new HttpRequestMethodNotSupportedException("PUT", allowedMethods), //
				400, "methodNotAllowed");
		HttpHeaders headers = response.getHeaders();
		assertEquals(1, headers.size());
		Set<String> actualAllow = new HashSet<String>();

		for (HttpMethod method : headers.getAllow()) {
			actualAllow.add(method.name());
		}
		assertEquals(allowedMethods, actualAllow);

		allowedMethods = Collections.emptySet();
		response = //
		performTest(new HttpRequestMethodNotSupportedException("PUT", allowedMethods), //
				400, "methodNotAllowed");
		headers = response.getHeaders();
		assertEquals(0, headers.size());
	}

	@Test
	public void testHttpMediaTypeNotSupportedException() {
		List<MediaType> supportedMediaTypes = Lists.newArrayList(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON);
		ResponseEntity<Object> response = //
		performTest(new HttpMediaTypeNotSupportedException(MediaType.TEXT_PLAIN, supportedMediaTypes), //
				400, "mediaTypeNotSupported");
		HttpHeaders headers = response.getHeaders();
		assertEquals(1, headers.size());
		assertEquals(supportedMediaTypes, headers.getAccept());

		supportedMediaTypes = Collections.emptyList();
		response = //
		performTest(new HttpMediaTypeNotSupportedException(MediaType.TEXT_PLAIN, supportedMediaTypes), //
				400, "mediaTypeNotSupported");
		headers = response.getHeaders();
		assertEquals(0, headers.size());
	}

	@Test
	public void testHttpMediaTypeNotAcceptableException() {
		List<MediaType> supportedMediaTypes = Lists.newArrayList(//
				MediaType.APPLICATION_XML, //
				MediaType.APPLICATION_JSON);

		performTest(//
				new HttpMediaTypeNotAcceptableException(supportedMediaTypes), //
				400, //
				"mediaTypeNotAcceptable");
	}

	@Test
	public void testMissingServletRequestParameterException() {
		performTest(//
				new MissingServletRequestParameterException("test", "String"), //
				400, //
				"missingParameter");
	}

	@Test
	public void testServletRequestBindingException() {
		performTest(//
				new ServletRequestBindingException("test"), //
				400, //
				"bindingException");
	}

	@Test
	public void testConversionNotSupportedException() {
		performTest(//
				new ConversionNotSupportedException("test", Object.class, null), //
				400, //
				"conversionNotSupported");
	}

	@Test
	public void testTypeMismatchException() {
		performTest(//
				new TypeMismatchException("test", Object.class), //
				400, //
				"typeMismatch");
	}

	@Test
	public void testHttpMessageNotReadableException() {
		performTest(//
				new HttpMessageNotReadableException("test"), //
				400, //
				"messageNotReadable");
	}

	@Test
	public void testHttpMessageNotWritableException() {
		performTest(//
				new HttpMessageNotWritableException("test"), //
				400, //
				"messageNotWritable");
	}

	@Test
	public void testMethodArgumentNotValidException() {
		performTest(//
				new MethodArgumentNotValidException(null, null), //
				400, //
				"invalidParameter");
	}

	@Test
	public void testMissingServletRequestPartException() {
		performTest(//
				new MissingServletRequestPartException("test"), //
				400, //
				"missingRequestPart");
	}

	@Test
	public void testBindException() {
		performTest(//
				new BindException(new Object(), "test"), //
				400, //
				"bindingException");
	}

	@Test
	public void testNoHandlerFoundException() {
		performTest(//
				new NoHandlerFoundException("test", "test", null), //
				404, //
				"notFound");
	}

	@Test
	public void testGeneralException() {
		Exception ex = new Exception("test");
		performTest(ex, 500, "internalError");

		verify(webRequest).setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
	}

	private ResponseEntity<Object> performTest(Exception e, int status, String reason) {
		ResponseEntity<Object> response = hander.handleException(e, webRequest);
		ApiError actual = (ApiError) response.getBody();
		assertEquals(status, response.getStatusCode().value());
		assertEquals(reason, actual.getReason());

		return response;
	}

	private void setError(int status, String reason) {
		ApiError error = new ApiError(status, reason, "message", "developerMessage", "errorCode");
		when(apiErrorFactory.error(reason)).thenReturn(error);
	}
}
