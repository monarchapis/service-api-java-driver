package com.monarchapis.driver.jaxrs.jersey1;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.authentication.Authenticator;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class ApiResourceFilterFactory implements ResourceFilterFactory {
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		List<ResourceFilter> filters = new LinkedList<ResourceFilter>();

		ApiOperation apiOperation = am.getAnnotation(ApiOperation.class);
		String operation = apiOperation != null ? apiOperation.value() : am.getMethod().getName();
		filters.add((ResourceFilter) new OperationNameResourceFilter(operation));

		ApiVersion apiVersion = getAnnotation(am, ApiVersion.class);

		if (apiVersion != null) {
			filters.add((ResourceFilter) new VersionResourceFilter(apiVersion.value()));
		}

		BypassAnalytics bypassAnalytics = getAnnotation(am, BypassAnalytics.class);

		if (bypassAnalytics != null) {
			filters.add((ResourceFilter) new BypassAnalyticsResourceFilter());
		}

		Authorize authorize = getAnnotation(am, Authorize.class);

		if (authorize != null) {
			RequestWeight requestWeight = am.getAnnotation(RequestWeight.class);

			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value())
					: Authenticator.NORMAL_WEIGHT;

			filters.add((ResourceFilter) new AuthorizeResourceFilter(authorize.client(), authorize.user(), authorize
					.delegated(), authorize.claims(), weight));
		}

		return filters;
	}

	private <T extends Annotation> T getAnnotation(AbstractMethod am, Class<T> clazz) {
		T annotation = null;

		if (am.isAnnotationPresent(clazz)) {
			annotation = am.getAnnotation(clazz);
		}

		if (annotation == null) {
			AbstractResource ar = am.getResource();

			if (ar.isAnnotationPresent(clazz)) {
				annotation = ar.getAnnotation(clazz);
			}
		}

		return annotation;
	}
}
