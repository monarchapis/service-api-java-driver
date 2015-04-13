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

package com.monarchapis.driver.jaxrs.v2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.authentication.Authenticator;

/**
 * Applies various request filters to methods of resource classes.
 * 
 * @author Phil Kedy
 */
@Provider
@PreMatching
public class ApiDynamicFeature implements DynamicFeature {
	@Override
	public void configure(final ResourceInfo resourceInfo, FeatureContext configuration) {
		Method method = resourceInfo.getResourceMethod();

		ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		String operation = apiOperation != null ? apiOperation.value() : method.getName();
		configuration.register(new OperationNameRequestFilter(operation));

		ApiVersion apiVersion = getAnnotation(method, ApiVersion.class);

		if (apiVersion != null) {
			configuration.register(new VersionRequestFilter(apiVersion.value()));
		}

		BypassAnalytics bypassAnalytics = getAnnotation(method, BypassAnalytics.class);

		if (bypassAnalytics != null) {
			configuration.register(new BypassAnalyticsRequestFilter());
		}

		Authorize authorize = getAnnotation(method, Authorize.class);

		if (authorize != null) {
			RequestWeight requestWeight = method.getAnnotation(RequestWeight.class);
			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value())
					: Authenticator.NORMAL_WEIGHT;

			configuration.register(new AuthorizeRequestFilter(authorize.client(), authorize.user(), authorize
					.delegated(), authorize.claims(), weight));
		}
	}

	/**
	 * Find an annotation first at the method level then at the class level.
	 * 
	 * @param method
	 *            The method
	 * @param clazz
	 *            The annotation class to find
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
}
