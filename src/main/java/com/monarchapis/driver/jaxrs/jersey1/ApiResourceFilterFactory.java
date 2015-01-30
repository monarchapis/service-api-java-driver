package com.monarchapis.driver.jaxrs.jersey1;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.RequestWeight;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class ApiResourceFilterFactory implements ResourceFilterFactory {
	private static final BigDecimal DEFAULT_WEIGHT = new BigDecimal(1);

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		List<ResourceFilter> filters = new LinkedList<ResourceFilter>();

		ApiOperation apiOperation = am.getAnnotation(ApiOperation.class);
		String operation = apiOperation != null ? apiOperation.value() : am.getMethod().getName();
		filters.add((ResourceFilter) new OperationNameResourceFilter(operation));

		Authorize authorize = null;

		if (am.isAnnotationPresent(Authorize.class)) {
			authorize = am.getAnnotation(Authorize.class);
		}

		if (authorize == null) {
			AbstractResource ar = am.getResource();

			if (ar.isAnnotationPresent(Authorize.class)) {
				authorize = ar.getAnnotation(Authorize.class);
			}
		}

		if (authorize != null) {
			RequestWeight requestWeight = am.getAnnotation(RequestWeight.class);

			BigDecimal weight = requestWeight != null ? new BigDecimal(requestWeight.value()) : DEFAULT_WEIGHT;

			filters.add((ResourceFilter) new AuthorizeResourceFilter(authorize.client(), authorize.user(), authorize
					.delegated(), authorize.claims(), weight));
		}

		return filters.isEmpty() ? null : filters;
	}
}
