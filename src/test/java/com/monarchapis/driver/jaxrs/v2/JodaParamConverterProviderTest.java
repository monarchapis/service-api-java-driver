package com.monarchapis.driver.jaxrs.v2;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

public class JodaParamConverterProviderTest {
	private JodaParamConverterProvider provider = new JodaParamConverterProvider();

	@Test
	public void testDateTimeWithMillis() {
		ParamConverter<DateTime> converter = provider.getConverter(DateTime.class, (Type) null, (Annotation[]) null);

		String value = "2015-04-01T12:00:00.000-04:00";
		DateTime dateTime = converter.fromString(value);
		String actual = converter.toString(dateTime);
		assertEquals(value, actual);
	}

	@Test
	public void testDateTimeNoMillis() {
		ParamConverter<DateTime> converter = provider.getConverter(DateTime.class, (Type) null, (Annotation[]) null);

		String value = "2015-04-01T12:00:00-04:00";
		DateTime dateTime = converter.fromString(value);
		String actual = converter.toString(dateTime);
		assertEquals("2015-04-01T12:00:00.000-04:00", actual);
	}

	@Test
	public void testLocalDate() {
		ParamConverter<LocalDate> converter = provider.getConverter(LocalDate.class, (Type) null, (Annotation[]) null);

		String value = "2015-04-01";
		LocalDate dateTime = converter.fromString(value);
		String actual = converter.toString(dateTime);
		assertEquals(value, actual);
	}

	@Test
	public void testUnknown() {
		ParamConverter<Object> converter = provider.getConverter(Object.class, (Type) null, (Annotation[]) null);
		assertNull(converter);
	}
}
