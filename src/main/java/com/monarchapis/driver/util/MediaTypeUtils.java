package com.monarchapis.driver.util;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.monarchapis.driver.servlet.ApiRequest;

public abstract class MediaTypeUtils {
	private static List<String> supportedMimeTypes = Lists.newArrayList(//
			MediaType.APPLICATION_JSON, //
			MediaType.APPLICATION_XML);

	private MediaTypeUtils() {
	}

	public static String getBestMediaType() {
		return getBestMediaType(ApiRequest.getCurrent());
	}

	public static String getBestMediaType(ApiRequest request) {
		String mediaType = null;
		String accept = request.getHeader("Accept");

		if (StringUtils.isNotBlank(accept)) {
			mediaType = MIMEParse.bestMatch(supportedMimeTypes, accept);
		}

		if (StringUtils.isBlank(mediaType)) {
			mediaType = MediaType.APPLICATION_JSON;
		}

		return mediaType;
	}
}
