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

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;

/**
 * Uses the Hawk authentication protocol to sign an API request.
 * 
 * @author Phil Kedy
 */
public class HawkV1AuthenticationSigner implements AuthenticationSigner {
	/**
	 * Flag that denotes that request payload verification is enabled.
	 */
	private boolean requestPayloadVerification;

	public HawkV1AuthenticationSigner() {
		setRequestPayloadVerification(true);
	}

	public HawkV1AuthenticationSigner(boolean requestPayloadVerification) {
		setRequestPayloadVerification(requestPayloadVerification);
	}

	/**
	 * Calculates the payload hash (if enabled), creates the Hawk header, and
	 * sets the Authorization header.
	 */
	@Override
	public void signRequest(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken) {
		HttpHeaders headers = request.getHeaders();
		String payloadHash = requestPayloadVerification ? createHawkHash(request) : null;
		String header = createHawkHeader(request, algorithm, apiKey, sharedSecret, accessToken, payloadHash, null);
		headers.setAuthorization(header);
	}

	/**
	 * Creates the Hawk request hash.
	 * 
	 * @param request
	 *            The API request
	 * @return a Base64 string representing the hash.
	 */
	public String createHawkHash(HttpRequest request) {
		try {
			StringBuilder sb = new StringBuilder();
			HttpContent httpContent = request.getContent();
			String mimeType = "";
			String content = "";

			if (httpContent != null) {
				mimeType = StringUtils.trim(StringUtils.substringBefore(httpContent.getType(), ";"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				httpContent.writeTo(baos);
				content = baos.toString("UTF-8");
			}

			sb.append("hawk.1.payload\n");
			sb.append(mimeType);
			sb.append("\n");
			sb.append(content);
			sb.append("\n");

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(sb.toString().getBytes("UTF-8"));
			return Base64.encodeBase64String(hash);
		} catch (Exception e) {
			throw new RuntimeException("Could not create hawk hash", e);
		}
	}

	/**
	 * Creates the hawk header
	 * 
	 * @param request
	 *            The API request
	 * @param algorithm
	 *            The HMac algorithm to use
	 * @param apiKey
	 *            The client/provider API key
	 * @param sharedSecret
	 *            The client/provider shared secret
	 * @param accessToken
	 *            The optional access token
	 * @param payloadHash
	 *            The request payload hash
	 * @param extData
	 *            The optional extended data
	 * @return a string representing Authorization header for Hawk.
	 */
	public String createHawkHeader(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken, String payloadHash, String extData) {
		try {
			StringBuilder sb = new StringBuilder();

			long ts = getTimestamp();
			String nonce = getNonce();
			GenericUrl url = request.getUrl();

			int port = url.getPort();

			if (port == -1) {
				port = "https".equals(url.getScheme()) ? 443 : 80;
			}

			sb.append("hawk.1.header\n");
			sb.append(ts);
			sb.append("\n");
			sb.append(nonce);
			sb.append("\n");
			sb.append(request.getRequestMethod());
			sb.append("\n");
			sb.append(url.getRawPath());

			String queryString = StringUtils.substringAfter(url.toString(), "?");
			if (StringUtils.isNotBlank(queryString)) {
				sb.append('?');
				sb.append(queryString);
			}

			sb.append("\n");
			sb.append(url.getHost());
			sb.append("\n");
			sb.append(port);
			sb.append("\n");

			if (payloadHash != null) {
				sb.append(payloadHash);
			}

			sb.append("\n");

			if (extData != null) {
				sb.append(extData);
			}

			sb.append("\n");

			if (accessToken != null) {
				sb.append(apiKey);
				sb.append("\n");
			}

			String stringData = sb.toString();

			String algo = HmacUtils.getHMacAlgorithm(algorithm);
			byte[] key = sharedSecret.getBytes("UTF-8");
			SecretKeySpec signingKey = new SecretKeySpec(key, algo);

			Mac macInstance = Mac.getInstance(algo);
			macInstance.init(signingKey);

			// compute the hmac on input data bytes
			byte[] hash = macInstance.doFinal(stringData.getBytes("UTF-8"));
			String mac = Base64.encodeBase64String(hash);

			return "Hawk id=\"" + (accessToken != null ? accessToken : apiKey) + "\", ts=\"" + ts + "\", nonce=\""
					+ nonce + "\"" + (payloadHash != null ? ", hash=\"" + payloadHash + "\"" : "")
					+ (extData != null ? ", ext=\"" + extData + "\"" : "") + ", mac=\"" + mac + "\""
					+ (accessToken != null ? ", app=\"" + apiKey + "\"" : "");
		} catch (Exception e) {
			throw new RuntimeException("Could not create hawk header", e);
		}
	}

	/**
	 * Returns a random nonce.
	 * 
	 * @return a random nonce.
	 */
	protected String getNonce() {
		return RandomStringUtils.randomAlphanumeric(6);
	}

	/**
	 * Returns the current time stamp in seconds.
	 * 
	 * @return the current time stamp
	 */
	protected long getTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	public boolean isRequestPayloadVerification() {
		return requestPayloadVerification;
	}

	public void setRequestPayloadVerification(boolean requestPayloadVerification) {
		this.requestPayloadVerification = requestPayloadVerification;
	}
}
