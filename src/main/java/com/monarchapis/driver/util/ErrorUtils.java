package com.monarchapis.driver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpResponseException;
import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.exception.ApiErrorException;
import com.monarchapis.driver.exception.ApiException;
import com.monarchapis.driver.servlet.ApiRequest;

public abstract class ErrorUtils {
	private static final Logger log = LoggerFactory.getLogger(ErrorUtils.class);

	private static List<String> supportedMimeTypes;

	static {
		supportedMimeTypes = new ArrayList<String>(2);
		supportedMimeTypes.add(MediaType.APPLICATION_JSON);
		supportedMimeTypes.add(MediaType.APPLICATION_XML);
	}

	private ErrorUtils() {
	}

	public static ApiException processApiError(HttpResponseException hre) {
		ApiError error = parseApiError(hre);

		return (error != null) ? new ApiErrorException(error) : new ApiException("Could not process error response");
	}

	public static ApiError parseApiError(HttpResponseException hre) {
		ApiError error = null;
		String content = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			content = hre.getContent();

			if (content != null) {
				error = mapper.readValue(content, ApiError.class);
			}
		} catch (IOException ioe) {
			// Ignore and fall through
			log.debug("Could not read error response: {}", content);
		}

		return error;
	}

	public static String getBestMediaType() {
		return getBestMediaType(ApiRequest.getCurrent());
	}

	public static String getBestMediaType(ApiRequest request) {
		String mediaType = null;
		String accept = request.getHeader("Accept");

		if (accept != null) {
			mediaType = MIMEParse.bestMatch(supportedMimeTypes, accept);
		}

		if (mediaType == null) {
			mediaType = MediaType.APPLICATION_JSON;
		}

		return mediaType;
	}
}
