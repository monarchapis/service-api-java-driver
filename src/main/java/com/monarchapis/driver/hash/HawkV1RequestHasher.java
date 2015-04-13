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

package com.monarchapis.driver.hash;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.servlet.ApiRequest;

/**
 * Handles creating a request hash for an API request using the Hawk protocol.
 * 
 * @author Phil Kedy
 */
public class HawkV1RequestHasher implements RequestHasher {
	@Override
	public String getName() {
		return "Hawk V1";
	}

	/**
	 * Creates a Hawk payload hash.
	 */
	@Override
	public String getRequestHash(ApiRequest request, String algorithm) {
		try {
			String digestAlgorithm = HasherUtils.getMessageDigestAlgorithm(algorithm);
			String mimeType = StringUtils.substringBefore(request.getContentType(), ";");

			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos.write("hawk.1.payload\n".getBytes("UTF-8"));

			if (mimeType != null) {
				baos.write(mimeType.getBytes("UTF-8"));
			}

			baos.write('\n');
			baos.write(request.getBody());
			baos.write('\n');
			byte[] hash = digest.digest(baos.toByteArray());

			return Base64.encodeBase64String(hash);
		} catch (Exception e) {
			throw new ApiException("Could not generate request hash", e);
		}
	}
}
