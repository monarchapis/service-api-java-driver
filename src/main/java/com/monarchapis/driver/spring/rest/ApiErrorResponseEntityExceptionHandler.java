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

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.model.ErrorHolder;

/**
 * Maps ApiErrorException and Spring Rest exceptions to ResponseEntity objects.
 * 
 * @author Phil Kedy
 */
@Component
@ControllerAdvice
public class ApiErrorResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Inject
	private ApiErrorFactory apiErrorFactory;

	@ExceptionHandler(ApiErrorException.class)
	public ResponseEntity<ApiError> handleApiErrorException(HttpServletRequest req, ApiErrorException ex) {
		ApiError error = ex.getError();
		HttpStatus status = HttpStatus.valueOf(error.getStatus());

		return new ResponseEntity<ApiError>(setErrorHolder(error), status);
	}

	@ExceptionHandler(com.monarchapis.api.exception.ApiErrorException.class)
	public ResponseEntity<ApiError> handleApiErrorException(HttpServletRequest req,
			com.monarchapis.api.exception.ApiErrorException ex) {
		com.monarchapis.api.exception.ApiError error = ex.getError();
		HttpStatus status = HttpStatus.valueOf(error.getCode());

		ApiError e = new ApiError(error.getCode(), error.getReason(), error.getMessage(), error.getDeveloperMessage(),
				error.getErrorCode(), error.getMoreInfo());

		return new ResponseEntity<ApiError>(setErrorHolder(e), status);
	}

	protected ResponseEntity<Object> handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("notFound", headers);
	}

	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();

		if (!supportedMethods.isEmpty()) {
			headers.setAllow(supportedMethods);
		}

		return errorResponse("methodNotAllowed", headers);
	}

	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<MediaType> mediaTypes = ex.getSupportedMediaTypes();

		if (!CollectionUtils.isEmpty(mediaTypes)) {
			headers.setAccept(mediaTypes);
		}

		return errorResponse("mediaTypeNotSupported", headers);
	}

	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("mediaTypeNotAcceptable", headers);
	}

	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("missingParameter", headers);
	}

	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("bindingException", headers);
	}

	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("conversionNotSupported", headers);
	}

	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return errorResponse("typeMismatch", headers);
	}

	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("messageNotReadable", headers);
	}

	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("messageNotWritable", headers);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("invalidParameter", headers);
	}

	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return errorResponse("missingRequestPart", headers);
	}

	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return errorResponse("bindingException", headers);
	}

	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return errorResponse("notFound", headers);
	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
		}

		return errorResponse("internalError", headers);
	}

	private ResponseEntity<Object> errorResponse(String errorReason, HttpHeaders headers) {
		ApiError error = apiErrorFactory.error(errorReason);

		return new ResponseEntity<Object>(setErrorHolder(error), headers, HttpStatus.valueOf(error.getStatus()));
	}

	private ApiError setErrorHolder(ApiError error) {
		ErrorHolder.setCurrent(error);

		return error;
	}

	public void setApiErrorFactory(ApiErrorFactory apiErrorFactory) {
		this.apiErrorFactory = apiErrorFactory;
	}
}
