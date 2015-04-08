package com.monarchapis.driver.servlet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.base.Optional;
import com.monarchapis.driver.analytics.AnalyticsHandler;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.BypassAnalyticsHolder;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.util.ServiceResolver;

@RunWith(MockitoJUnitRunner.class)
public class ApiFilterTest {
	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain chain;

	@Mock
	private ServiceResolver serviceResolver;

	@Mock
	private AnalyticsHandler analyticsHandler;

	private ApiFilter filter = new ApiFilter();

	@Before
	public void setup() throws IOException {
		ServiceResolver.setInstance(serviceResolver);

		when(request.getInputStream()).thenAnswer(new Answer<InputStream>() {
			@Override
			public ServletInputStream answer(InvocationOnMock invocation) throws Throwable {
				return new ServletInputStreamWrapper("This is a test".getBytes());
			}
		});
	}

	@After
	public void teardown() {
		ApiRequest.remove();
		ErrorHolder.remove();
		BypassAnalyticsHolder.remove();
		ServiceResolver.setInstance(null);
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
				assertTrue(request instanceof ApiRequest);
				assertTrue(response instanceof ApiResponse);

				ApiContext context = ApiContext.getCurrent();
				assertNotNull(context);

				ApiRequest apiRequest = ApiRequest.getCurrent();
				assertNotNull(apiRequest);
			}
		};

		filter.doFilter(request, response, chain);
	}

	@Test
	public void testBypassAnalytics() throws IOException, ServletException {
		filter.doFilter(request, response, chain);
		verify(analyticsHandler, never()).collect(any(ApiRequest.class), any(ApiResponse.class), anyLong());

		filter.doFilter(request, response, chain);
		BypassAnalyticsHolder.setCurrent(true);
		verify(analyticsHandler, never()).collect(any(ApiRequest.class), any(ApiResponse.class), anyLong());

		when(serviceResolver.getOptional(AnalyticsHandler.class)).thenReturn(Optional.of(analyticsHandler));
		filter.doFilter(request, response, chain);
		verify(analyticsHandler, never()).collect(any(ApiRequest.class), any(ApiResponse.class), anyLong());
	}

	@Test
	public void testWithAnalytics() throws IOException, ServletException {
		when(serviceResolver.getOptional(AnalyticsHandler.class)).thenReturn(Optional.of(analyticsHandler));
		filter.doFilter(request, response, chain);
		verify(analyticsHandler).collect(any(ApiRequest.class), any(ApiResponse.class), anyLong());
	}
}
