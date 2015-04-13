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

package com.monarchapis.driver.authentication;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

@RunWith(MockitoJUnitRunner.class)
public class HawkV1AuthenticationSignerTest {
	private static final String ID = "dh37fgj492je";
	private static final String KEY = "werxhqb98rpaxn39848xrunpaw3489ruxnpa98w4rxn";
	private static final String ALGORITHM = "sha256";
	private static final String ACCESS_TOKEN = "werxhqb98rpaxn39848";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private static final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

	private HttpRequest request;

	@InjectMocks
	private TestHawkV1AuthenticationSigner signer;

	@Before
	public void setup() throws IOException {
		GenericUrl url = new GenericUrl("http://example.com:8000/resource/1?a=1&b=2");
		HttpContent httpContent = new ByteArrayContent("text/plain; utf-8", "Thank you for flying Hawk".getBytes());
		request = requestFactory.buildPostRequest(url, httpContent);
	}

	@Test
	public void testCreateHawkHash() {
		String actual = signer.createHawkHash(request);
		assertEquals("Yi9LfIIFRtBEPt74PVmbTF/xVAwPn7ub15ePICfgnuY=", actual);
	}

	@Test
	public void testCreateHawkHeader() {
		String actual = signer.createHawkHeader(request, ALGORITHM, ID, KEY, null,
				"Yi9LfIIFRtBEPt74PVmbTF/xVAwPn7ub15ePICfgnuY=", "some-app-ext-data");
		assertEquals(
				"Hawk id=\"dh37fgj492je\", ts=\"1353832234\", nonce=\"j4h3g2\", hash=\"Yi9LfIIFRtBEPt74PVmbTF/xVAwPn7ub15ePICfgnuY=\", ext=\"some-app-ext-data\", mac=\"5BTCLzyOXyOa1T78zgcVhOZWL5FV/5y3eMbSYjRj3uA=\"",
				actual);
	}

	@Test
	public void testSignRequest() {
		signer.signRequest(request, ALGORITHM, ID, KEY, null);
		assertEquals(
				"Hawk id=\"dh37fgj492je\", ts=\"1353832234\", nonce=\"j4h3g2\", hash=\"Yi9LfIIFRtBEPt74PVmbTF/xVAwPn7ub15ePICfgnuY=\", mac=\"mZSplfsJGilEROddOoVenGPTQvmomwUEa+dbX+pNtRk=\"",
				request.getHeaders().getAuthorization());
	}

	@Test
	public void testWithoutRequestHash() {
		TestHawkV1AuthenticationSigner signer = new TestHawkV1AuthenticationSigner(false);
		signer.signRequest(request, ALGORITHM, ID, KEY, null);
		assertEquals(
				"Hawk id=\"dh37fgj492je\", ts=\"1353832234\", nonce=\"j4h3g2\", mac=\"sgdmZS8tS0rwno7hrj6qiKY8dNvWoWIwv5QXAl5IZvw=\"",
				request.getHeaders().getAuthorization());
	}
	
	@Test
	public void testWithAccessToken() {
		TestHawkV1AuthenticationSigner signer = new TestHawkV1AuthenticationSigner(false);
		signer.signRequest(request, ALGORITHM, ID, KEY, ACCESS_TOKEN);
		assertEquals(
				"Hawk id=\"" + ACCESS_TOKEN + "\", ts=\"1353832234\", nonce=\"j4h3g2\", mac=\"XZHL3KZrV5oZIIbDgRyJcLrsHYQtzTXcHs0y6P5RSNI=\", app=\"dh37fgj492je\"",
				request.getHeaders().getAuthorization());
	}

	private static class TestHawkV1AuthenticationSigner extends HawkV1AuthenticationSigner {
		@SuppressWarnings("unused")
		public TestHawkV1AuthenticationSigner() {
			super();
		}

		public TestHawkV1AuthenticationSigner(boolean requestPayloadVerification) {
			super(requestPayloadVerification);
		}

		protected String getNonce() {
			return "j4h3g2";
		}

		protected long getTimestamp() {
			return 1353832234;
		}
	}
}
