package com.monarchapis.driver.jaxrs.jersey1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ResourceFilter;

@RunWith(MockitoJUnitRunner.class)
public class ApiResourceFilterFactoryTest {
	@Mock
	private ApiOperation operation;

	@Mock
	private ApiVersion version;

	@Mock
	private BypassAnalytics bypassAnalytics;

	@Mock
	private Authorize authorize;

	@Mock
	private Claim claim;

	@Mock
	private RequestWeight requestWeight;

	@Mock
	private AbstractMethod method;

	private Method javaMethod = getMethod();

	@Mock
	private AbstractResource resource;

	@Mock
	private ContainerRequest containerRequest;

	private ApiResourceFilterFactory factory = new ApiResourceFilterFactory();

	public void someMethod() {
		// This is a dummy method
	}

	private Method getMethod() {
		try {
			return getClass().getMethod("someMethod");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Before
	public void setup() {
		when(operation.value()).thenReturn("test");
		when(version.value()).thenReturn("v1");
		when(requestWeight.value()).thenReturn("2");
		when(method.getResource()).thenReturn(resource);
		when(method.getMethod()).thenReturn(javaMethod);

		when(authorize.client()).thenReturn(new String[] { "client" });
		when(authorize.delegated()).thenReturn(new String[] { "delegated" });
		when(authorize.claims()).thenReturn(new Claim[] { claim });
		when(authorize.user()).thenReturn(true);
	}

	@After
	public void teardown() {
		OperationNameHolder.remove();
		VersionHolder.remove();
		BypassAnalyticsHolder.remove();
	}

	// Will use the actual java method name when the ApiOperation annotation is
	// not present
	@Test
	public void testWithoutOperation() {
		List<ResourceFilter> filters = factory.create(method);
		OperationNameResourceFilter filter = findFilterClass(filters, OperationNameResourceFilter.class);
		assertNotNull(filter);
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame("someMethod", OperationNameHolder.getCurrent());
	}

	// Will use the value set in the ApiOperation annotation if present
	@Test
	public void testWithOperation() {
		when(method.getAnnotation(ApiOperation.class)).thenReturn(operation);
		List<ResourceFilter> filters = factory.create(method);
		OperationNameResourceFilter filter = findFilterClass(filters, OperationNameResourceFilter.class);
		assertNotNull(filter);
		ContainerRequest result = filter.filter(containerRequest);
		assertSame(containerRequest, result);
		assertSame("test", OperationNameHolder.getCurrent());
	}

	@Test
	public void testVersionOnMethod() {
		when(method.isAnnotationPresent(ApiVersion.class)).thenReturn(true);
		when(method.getAnnotation(ApiVersion.class)).thenReturn(version);
		List<ResourceFilter> filters = factory.create(method);
		VersionResourceFilter filter = findFilterClass(filters, VersionResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testVersionOnClass() {
		when(method.isAnnotationPresent(ApiVersion.class)).thenReturn(false);
		when(resource.isAnnotationPresent(ApiVersion.class)).thenReturn(true);
		when(resource.getAnnotation(ApiVersion.class)).thenReturn(version);
		List<ResourceFilter> filters = factory.create(method);
		VersionResourceFilter filter = findFilterClass(filters, VersionResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testBypassAnalyticsOnMethod() {
		when(method.isAnnotationPresent(BypassAnalytics.class)).thenReturn(true);
		when(method.getAnnotation(BypassAnalytics.class)).thenReturn(bypassAnalytics);
		List<ResourceFilter> filters = factory.create(method);
		BypassAnalyticsResourceFilter filter = findFilterClass(filters, BypassAnalyticsResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testBypassAnalyticsOnClass() {
		when(method.isAnnotationPresent(BypassAnalytics.class)).thenReturn(false);
		when(resource.isAnnotationPresent(BypassAnalytics.class)).thenReturn(true);
		when(resource.getAnnotation(BypassAnalytics.class)).thenReturn(bypassAnalytics);
		List<ResourceFilter> filters = factory.create(method);
		BypassAnalyticsResourceFilter filter = findFilterClass(filters, BypassAnalyticsResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testAuthorizeOnMethod() {
		when(method.isAnnotationPresent(Authorize.class)).thenReturn(true);
		when(method.getAnnotation(Authorize.class)).thenReturn(authorize);
		List<ResourceFilter> filters = factory.create(method);
		AuthorizeResourceFilter filter = findFilterClass(filters, AuthorizeResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testAuthorizeOnClass() {
		when(method.isAnnotationPresent(Authorize.class)).thenReturn(false);
		when(resource.isAnnotationPresent(Authorize.class)).thenReturn(true);
		when(resource.getAnnotation(Authorize.class)).thenReturn(authorize);
		List<ResourceFilter> filters = factory.create(method);
		AuthorizeResourceFilter filter = findFilterClass(filters, AuthorizeResourceFilter.class);
		assertNotNull(filter);
	}

	@Test
	public void testRequestWeight() {
		when(method.isAnnotationPresent(Authorize.class)).thenReturn(true);
		when(method.getAnnotation(Authorize.class)).thenReturn(authorize);
		when(method.isAnnotationPresent(RequestWeight.class)).thenReturn(true);
		when(method.getAnnotation(RequestWeight.class)).thenReturn(requestWeight);
		List<ResourceFilter> filters = factory.create(method);
		AuthorizeResourceFilter filter = findFilterClass(filters, AuthorizeResourceFilter.class);
		assertNotNull(filter);
		assertEquals(new BigDecimal("2"), filter.getRequestWeight());
	}

	@SuppressWarnings("unchecked")
	private <T extends ResourceFilter> T findFilterClass(List<ResourceFilter> filters, Class<T> clazz) {
		assertNotNull(filters);

		for (ResourceFilter filter : filters) {
			if (clazz.isAssignableFrom(filter.getClass())) {
				return (T) filter;
			}
		}

		return null;
	}
}
