package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.monarchapis.driver.annotation.ApiOperation;
import com.monarchapis.driver.annotation.ApiVersion;
import com.monarchapis.driver.annotation.Authorize;
import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.annotation.RequestWeight;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.OperationNameHolder;
import com.monarchapis.driver.model.VersionHolder;

@RunWith(MockitoJUnitRunner.class)
public class ApiDynamicFeatureTest {
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
	private ResourceInfo resourceInfo;

	@Mock
	private FeatureContext configuration;

	private ApiDynamicFeature feature = new ApiDynamicFeature();

	private OperationNameRequestFilter operationNameRequestFilter;
	private VersionRequestFilter versionRequestFilter;
	private BypassAnalyticsRequestFilter bypassAnalyticsRequestFilter;
	private AuthorizeRequestFilter authorizeRequestFilter;

	@Before
	public void setup() {
		when(operation.value()).thenReturn("test");
		when(version.value()).thenReturn("v1");
		when(requestWeight.value()).thenReturn("2");

		when(authorize.client()).thenReturn(new String[] { "client" });
		when(authorize.delegated()).thenReturn(new String[] { "delegated" });
		when(authorize.claims()).thenReturn(new Claim[] { claim });
		when(authorize.user()).thenReturn(true);

		when(configuration.register(any())).thenAnswer(new Answer<FeatureContext>() {
			@Override
			public FeatureContext answer(InvocationOnMock invocation) throws Throwable {
				Object first = invocation.getArgumentAt(0, Object.class);
				operationNameRequestFilter = cast(first, OperationNameRequestFilter.class);
				versionRequestFilter = cast(first, VersionRequestFilter.class);
				bypassAnalyticsRequestFilter = cast(first, BypassAnalyticsRequestFilter.class);
				authorizeRequestFilter = cast(first, AuthorizeRequestFilter.class);

				return configuration;
			}

			@SuppressWarnings("unchecked")
			private <T> T cast(Object object, Class<T> clazz) {
				try {
					if (object != null) {
						if (clazz.isAssignableFrom(object.getClass())) {
							return (T) object;
						}
					}

					return null;
				} catch (ClassCastException cce) {
					return null;
				}
			}
		});
	}

	@After
	public void teardown() {
		operationNameRequestFilter = null;
		versionRequestFilter = null;
		bypassAnalyticsRequestFilter = null;
		authorizeRequestFilter = null;

		OperationNameHolder.remove();
		VersionHolder.remove();
		BypassAnalyticsHolder.remove();
	}

	// Will use the actual java method name when the ApiOperation annotation is
	// not present
	@Test
	public void testWithoutOperation() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(NoAnnotations.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(operationNameRequestFilter);
		assertEquals("someMethod", operationNameRequestFilter.getOperationName());
	}

	// Will use the value set in the ApiOperation annotation if present
	@Test
	public void testWithOperation() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(WithOperation.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(operationNameRequestFilter);
		assertEquals("test", operationNameRequestFilter.getOperationName());
	}

	@Test
	public void testVersionOnMethod() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(VersionOnMethod.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(versionRequestFilter);
		assertEquals("v1", versionRequestFilter.getVersion());
	}

	@Test
	public void testVersionOnClass() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(VersionOnClass.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(versionRequestFilter);
		assertEquals("v1", versionRequestFilter.getVersion());
	}

	@Test
	public void testBypassAnalyticsOnMethod() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(BypassAnalyticsOnMethod.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(bypassAnalyticsRequestFilter);
	}

	@Test
	public void testBypassAnalyticsOnClass() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(BypassAnalyticsOnClass.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(bypassAnalyticsRequestFilter);
	}

	@Test
	public void testAuthorizeOnMethod() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(AuthorizeOnMethod.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(authorizeRequestFilter);
	}

	@Test
	public void testAuthorizeOnClass() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(AuthorizeOnClass.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(authorizeRequestFilter);
	}

	@Test
	public void testRequestWeight() {
		when(resourceInfo.getResourceMethod()).thenReturn(getMethod(RequestWeightClass.class));
		feature.configure(resourceInfo, configuration);
		assertNotNull(authorizeRequestFilter);
		assertEquals(new BigDecimal("2"), authorizeRequestFilter.getRequestWeight());
	}

	private Method getMethod(Class<?> clazz) {
		try {
			return clazz.getMethod("someMethod");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private static class NoAnnotations {
		public void someMethod() {
		}
	}

	private static class WithOperation {
		@ApiOperation("test")
		public void someMethod() {
		}
	}

	private static class VersionOnMethod {
		@ApiVersion("v1")
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@ApiVersion("v1")
	private static class VersionOnClass {
		public void someMethod() {
		}
	}

	private static class BypassAnalyticsOnMethod {
		@BypassAnalytics
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@BypassAnalytics
	private static class BypassAnalyticsOnClass {
		public void someMethod() {
		}
	}

	private static class AuthorizeOnMethod {
		@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
		public void someMethod() {
		}
	}

	@SuppressWarnings("unused")
	@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
	private static class AuthorizeOnClass {
		public void someMethod() {
		}
	}

	private static class RequestWeightClass {
		@RequestWeight("2")
		@Authorize(client = "client", delegated = "delegated", user = true, claims = @Claim(type = "key", value = "value"))
		public void someMethod() {
		}
	}
}
