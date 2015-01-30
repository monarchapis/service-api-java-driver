package com.monarchapis.driver.jaxrs.v2;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.monarchapis.driver.annotation.ApiOperation;
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

		Authorize authorize = method.getAnnotation(Authorize.class);

		if (authorize == null) {
			authorize = method.getDeclaringClass().getAnnotation(Authorize.class);
		}

		RequestWeight requestWeight = method.getAnnotation(RequestWeight.class);

		BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value()) : DEFAULT_WEIGHT;

		if (authorize != null) {
			configuration.register(new AuthorizeRequestFilter(authorize.client(), authorize.user(), authorize
					.delegated(), authorize.claims(), weight));
		}
	}
}
