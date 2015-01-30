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

public class HawkV1AuthenticationSigner implements AuthenticationSigner {
	private boolean requestPayloadVerification;

	public HawkV1AuthenticationSigner() {
		this.requestPayloadVerification = true;
	}

	public HawkV1AuthenticationSigner(boolean requestPayloadVerification) {
		this.requestPayloadVerification = requestPayloadVerification;
	}

	@Override
	public void signRequest(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken) {
		HttpHeaders headers = request.getHeaders();
		String payloadHash = requestPayloadVerification ? getHawkHash(request) : null;
		String header = getHawkHeader(request, algorithm, apiKey, sharedSecret, accessToken, payloadHash, null);
		headers.setAuthorization(header);
	}

	private static String getHawkHash(HttpRequest request) {
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

	private String getHawkHeader(HttpRequest request, String algorithm, String apiKey, String sharedSecret,
			String accessToken, String payloadHash, String extData) {
		try {
			StringBuilder sb = new StringBuilder();

			long ts = System.currentTimeMillis() / 1000;
			String nonce = RandomStringUtils.randomAlphanumeric(6);
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
			byte[] key = sharedSecret.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(key, algo);

			Mac mac256 = Mac.getInstance(algo);
			mac256.init(signingKey);

			// compute the hmac on input data bytes
			byte[] hash = mac256.doFinal(stringData.getBytes("UTF-8"));
			String mac = Base64.encodeBase64String(hash);

			return "Hawk id=\"" + (accessToken != null ? accessToken : apiKey) + "\", ts=\"" + ts + "\", nonce=\""
					+ nonce + "\"" + (payloadHash != null ? ", hash=\"" + payloadHash + "\"" : "")
					+ (extData != null ? ", ext=\"" + extData + "\"," : "") + ", mac=\"" + mac + "\""
					+ (accessToken != null ? ", app=\"" + apiKey + "\"" : "");
		} catch (Exception e) {
			throw new RuntimeException("Could not create hawk header", e);
		}
	}
}
