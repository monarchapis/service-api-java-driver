package com.monarchapis.driver.exception;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.base.Optional;
import com.monarchapis.driver.configuration.ConfigurationBundle;
import com.monarchapis.driver.servlet.ApiRequest;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationBundleApiErrorFactoryTest {
	@Mock
	private ConfigurationBundle configurationBundle;

	@Mock
	private ApiRequest apiRequest;

	private Vector<Locale> locales;

	@InjectMocks
	private ConfigurationBundleApiErrorFactory apiErrorFactory;

	@Before
	public void setup() {
		ApiRequest.setCurrent(apiRequest);

		locales = new Vector<Locale>();
		when(apiRequest.getLocales()).thenAnswer(new Answer<Enumeration<Locale>>() {
			@Override
			public Enumeration<Locale> answer(InvocationOnMock invocation) throws Throwable {
				return locales.elements();
			}
		});

		String[] variants = new String[0];
		setTemplate("default", variants, 404, "test message", "test developer message", "test error code");
	}

	// Will use locale provided in the request as variants
	@Test
	public void testLocale() {
		locales.add(new Locale("fr", "CA"));
		String[] variants = new String[] { "fr-CA", "fr" };
		setTemplate("default", variants, 404, "test message FR", "test developer message FR", "test error code FR");

		ApiErrorException e = apiErrorFactory.exception("notFound");
		ApiError error = getError(e);

		assertEquals(404, error.getStatus());
		assertEquals("test message FR", error.getMessage());
		assertEquals("test developer message FR", error.getDeveloperMessage());
		assertEquals("test error code FR", error.getErrorCode());
	}

	// Will allow the use of templates for error properties
	@Test
	public void testTemplate() {
		String[] variants = new String[0];
		setTemplate("test", variants, 405, "test message 2", "test developer message 2", "test error code 2");

		ApiErrorException e = apiErrorFactory.exception("notFound", "test");
		ApiError error = getError(e);

		assertEquals(405, error.getStatus());
		assertEquals("test message 2", error.getMessage());
		assertEquals("test developer message 2", error.getDeveloperMessage());
		assertEquals("test error code 2", error.getErrorCode());
	}

	// Will pass arguments to be use in the ConfigurationBundle for message
	// formatting
	@Test
	public void testArgumentPassing() {
		String[] variants = new String[0];
		setTemplate("test", variants, 405, "test message {0}", "test developer message {0}", "test error code {0}",
				"hello");

		ApiErrorException e = apiErrorFactory.exception("notFound", "test", "hello");
		ApiError error = getError(e);

		// Formatting it actually performed in the ConfigurationBundle
		assertEquals(405, error.getStatus());
		assertEquals("test message {0}", error.getMessage());
		assertEquals("test developer message {0}", error.getDeveloperMessage());
		assertEquals("test error code {0}", error.getErrorCode());
	}

	// Will use the number of arguments as a template if the provided template
	// is not found
	@Test
	public void testTemplateFromNumberOfArguments() {
		String[] variants = new String[0];
		setTemplate("1", variants, 405, "test message args", "test developer message args", "test error code args",
				"hello");

		ApiErrorException e = apiErrorFactory.exception("notFound", null, "hello");
		ApiError error = getError(e);

		assertEquals(405, error.getStatus());
		assertEquals("test message args", error.getMessage());
		assertEquals("test developer message args", error.getDeveloperMessage());
		assertEquals("test error code args", error.getErrorCode());
	}

	// Will fall back on using the default template if no other template is
	// found or specified
	@Test
	public void testRevertToDefault() {
		String[] variants = new String[0];
		setTemplate("specific", variants, 405, "test message specific", "test developer message specific",
				"test error code specific");
		setTemplate("test", variants, null, null, null, null);

		ApiErrorException e = apiErrorFactory.exception("notFound", "test");
		ApiError error = getError(e);

		assertEquals(404, error.getStatus());
		assertEquals("test message", error.getMessage());
		assertEquals("test developer message", error.getDeveloperMessage());
		assertEquals("test error code", error.getErrorCode());
	}

	// Will set the more info error property based on a message format
	@Test
	public void testMoreInfo() {
		apiErrorFactory.setMoreInfoFormat("http://developer.company.com/errors/{0}");
		ApiErrorException e = apiErrorFactory.exception("notFound");
		ApiError error = getError(e);
		assertEquals("http://developer.company.com/errors/notFound", error.getMoreInfo());

		apiErrorFactory.setMoreInfoFormat("http://developer.company.com/errors/{1}");
		e = apiErrorFactory.exception("notFound");
		error = getError(e);
		assertEquals("http://developer.company.com/errors/test error code", error.getMoreInfo());
	}

	// Will throw a general system error if no error properties are found
	@Test
	public void testGeneralError() {
		ApiErrorException e = apiErrorFactory.exception("unknown");
		ApiError error = getError(e);

		assertEquals(500, error.getStatus());
		assertEquals("A general error has occurred.", error.getMessage());
		assertEquals("A general error has occurred.", error.getDeveloperMessage());
		assertEquals("SYSTEM-001", error.getErrorCode());
	}

	private void setTemplate(String template, String[] variants, Integer status, String message,
			String developerMessage, String errorCode, Object... args) {
		when(configurationBundle.getInteger("notFound." + template + ".status", variants)).thenReturn(
				Optional.fromNullable(status));
		when(configurationBundle.getString("notFound." + template + ".message", variants, args)).thenReturn(
				Optional.fromNullable(message));
		when(configurationBundle.getString("notFound." + template + ".developerMessage", variants, args)).thenReturn(
				Optional.fromNullable(developerMessage));
		when(configurationBundle.getString("notFound." + template + ".errorCode", variants, args)).thenReturn(
				Optional.fromNullable(errorCode));
	}

	private ApiError getError(ApiErrorException e) {
		assertNotNull(e);
		ApiError error = e.getError();
		assertNotNull(error);

		return error;
	}
}
