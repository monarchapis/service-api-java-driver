package com.monarchapis.driver.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class MIMEParseTest {
	@Test
	public void testParseMediaRange() {
		assertEquals("('application', 'xml', {'q':'1',})", MIMEParse.parseMediaRange("application/xml;q=1").toString());
		assertEquals("('application', 'xml', {'q':'1',})", MIMEParse.parseMediaRange("application/xml").toString());
		assertEquals("('application', 'xml', {'q':'1',})", MIMEParse.parseMediaRange("application/xml;q=").toString());
		assertEquals("('application', 'xml', {'q':'1',})", MIMEParse.parseMediaRange("application/xml ; q=").toString());
		assertEquals("('application', 'xml', {'b':'other','q':'1',})",
				MIMEParse.parseMediaRange("application/xml ; q=1;b=other").toString());
		assertEquals("('application', 'xml', {'b':'other','q':'1',})",
				MIMEParse.parseMediaRange("application/xml ; q=2;b=other").toString());
		// Java URLConnection class sends an Accept header that includes a
		// single *
		assertEquals("('*', '*', {'q':'.2',})", MIMEParse.parseMediaRange(" *; q=.2").toString());
	}

	@Test
	public void testRFC2616Example() {
		String accept = "text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5";

		assertEquals(1.0f, MIMEParse.quality("text/html;level=1", accept), 0);
		assertEquals(0.7f, MIMEParse.quality("text/html", accept), 0);
		assertEquals(0.3f, MIMEParse.quality("text/plain", accept), 0);
		assertEquals(0.5f, MIMEParse.quality("image/jpeg", accept), 0);
		assertEquals(0.4f, MIMEParse.quality("text/html;level=2", accept), 0);
		assertEquals(0.7f, MIMEParse.quality("text/html;level=3", accept), 0);
	}

	@Test
	public void testBestMatch() {
		List<String> mimeTypesSupported = Arrays.asList(StringUtils.split("application/xbel+xml,application/xml", ','));

		// direct match
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/xbel+xml"), "application/xbel+xml");

		// direct match with a q parameter
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/xbel+xml;q=1"), "application/xbel+xml");

		// direct match of our second choice with a q parameter
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/xml;q=1"), "application/xml");

		// match using a subtype wildcard
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/*;q=1"), "application/xml");

		// match using a type wildcard
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "*/*"), "application/xml");

		mimeTypesSupported = Arrays.asList(StringUtils.split("application/xbel+xml,text/xml", ','));

		// match using a type versus a lower weighted subtype
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "text/*;q=0.5,*/*;q=0.1"), "text/xml");

		// fail to match anything
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "text/html,application/atom+xml; q=0.9"), "");

		// common AJAX scenario
		mimeTypesSupported = Arrays.asList(StringUtils.split("application/json,text/html", ','));
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/json,text/javascript, */*"),
				"application/json");

		// verify fitness ordering
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "application/json,text/html;q=0.9"), "application/json");
	}

	@Test
	public void testSupportWildcards() {
		List<String> mimeTypesSupported = Arrays.asList(StringUtils.split("image/*,application/xml", ','));

		// match using a type wildcard
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "image/png"), "image/*");
		// match using a wildcard for both requested and supported
		assertEquals(MIMEParse.bestMatch(mimeTypesSupported, "image/*"), "image/*");
	}
}
