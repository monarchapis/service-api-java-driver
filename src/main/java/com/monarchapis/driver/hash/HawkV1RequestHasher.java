package com.monarchapis.driver.hash;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.servlet.ApiRequest;

public class HawkV1RequestHasher implements RequestHasher {
	@Override
	public String getName() {
		return "Hawk V1";
	}

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
