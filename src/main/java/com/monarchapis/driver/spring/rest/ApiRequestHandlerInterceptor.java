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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;

/**
 * Intercepts Spring Rest controller methods and invokes Monarch if
 * authentication is needed.
 * 
 * @author Phil Kedy
 */
@Component
public class ApiRequestHandlerInterceptor extends HandlerInterceptorAdapter {
	/**
	 * The authenticator implementation.
	 */
	@Inject
	private Authenticator authenticator;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			return preHandleMethod(request, response, (HandlerMethod) handler);
		} else {
			return true;
		}
	}

	/**
	 * Handles the {@link ApiOperation}, {@link ApiVersion},
	 * {@link BypassAnalytics}, and {@link Authorize} annotations.
	 * 
	 * @param request
	 *            The API request
	 * @param response
	 *            The API response
	 * @param handler
	 *            The handler method.
	 * @return true unless an exception is thrown by the security checks.
	 */
	private boolean preHandleMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		Method method = handler.getMethod();

		ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		String operation = apiOperation != null ? apiOperation.value() : method.getName();
		OperationNameHolder.setCurrent(operation);

		ApiVersion apiVersion = getAnnotation(method, ApiVersion.class);

		if (apiVersion != null) {
			VersionHolder.setCurrent(apiVersion.value());
		}

		BypassAnalytics bypassAnalytics = getAnnotation(method, BypassAnalytics.class);

		if (bypassAnalytics != null) {
			BypassAnalyticsHolder.setCurrent(true);
		}

		Authorize authorize = getAnnotation(method, Authorize.class);

		if (authorize != null) {
			RequestWeight requestWeight = method.getAnnotation(RequestWeight.class);
			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value())
					: Authenticator.NORMAL_WEIGHT;
			String[] client = authorize.client();
			String[] delegated = authorize.delegated();
			boolean user = authorize.user();
			Claim[] claims = authorize.claims();

			authenticator.performAccessChecks(weight, client, delegated, user, claims);
		}

		return true;
	}

	/**
	 * Looks for an annotation first on the method, then inspects the class.
	 * 
	 * @param method
	 *            The method search for the desired annotation.
	 * @param clazz
	 *            THe class to search if the annotation is not present on the
	 *            method.
	 * @return the annotation if found, null otherwise.
	 */
	private static <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
		T annotation = method.getAnnotation(clazz);

		if (annotation == null) {
			Class<?> declaringClass = method.getDeclaringClass();
			annotation = declaringClass.getAnnotation(clazz);

			while (annotation == null && declaringClass.getSuperclass() != null) {
				declaringClass = declaringClass.getSuperclass();
				annotation = declaringClass.getAnnotation(clazz);
			}
		}

		return annotation;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}
}