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
