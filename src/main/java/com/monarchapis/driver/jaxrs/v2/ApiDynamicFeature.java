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
import com.monarchapis.driver.annotation.RequestWeight;

@Provider
@PreMatching
public class ApiDynamicFeature implements DynamicFeature {
	private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);

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

		Authorize authorize = getAnnotation(method, Authorize.class);

		if (authorize != null) {
			RequestWeight requestWeight = method.getAnnotation(RequestWeight.class);
			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value()) : DEFAULT_WEIGHT;

			configuration.register(new AuthorizeRequestFilter(authorize.client(), authorize.user(), authorize
					.delegated(), authorize.claims(), weight));
		}
	}

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
