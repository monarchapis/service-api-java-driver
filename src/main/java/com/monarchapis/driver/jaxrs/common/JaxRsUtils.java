package com.monarchapis.driver.jaxrs.common;

import java.math.BigDecimal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.exception.ApiError;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.ErrorHolder;
import com.monarchapis.driver.service.v1.Authenticator;
import com.monarchapis.driver.util.ErrorUtils;

public class JaxRsUtils {
	private static final Logger logger = LoggerFactory.getLogger(JaxRsUtils.class);

	private static final BigDecimal NORMAL_WEIGHT = new BigDecimal("1");

	public static ApiContext getApiContext() {
		return getApiContext(NORMAL_WEIGHT);
	}

	public static ApiContext getApiContext(BigDecimal requestWeight) {
		ApiContext apiContext = ApiContext.getCurrent();

		if (apiContext.getClient() == null) {
			AuthenticationResponse authResponse = null;

			try {
				authResponse = Authenticator.processAuthentication(requestWeight);
			} catch (Exception e) {
				throwSystemException(e);
			}

			if (authResponse == null) {
				throwSystemException(null);
			}

			ApiContext responseContext = authResponse.getContext();

			if (responseContext != null) {
				apiContext.copy(responseContext);
			}

			if (authResponse.getCode() != 200) {
				ApiError error = new ApiError(authResponse.getCode(), authResponse.getReason(),
						authResponse.getMessage(), authResponse.getDeveloperMessage(), authResponse.getErrorCode(),
						null);

				ErrorHolder.setCurrent(error);

				throw new WebApplicationException(Response.status(error.getCode()).entity(error)
						.type(MediaType.APPLICATION_JSON).type(ErrorUtils.getBestMediaType()).build());
			}
		}

		return apiContext;
	}

	private static void throwSystemException(Exception e) {
		logger.error("Could not authenticate API request", e);

		ApiError error = new ApiError(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), "systemUnavailable",
				"The system is currently not available.  Please try again later.",
				"The service is unavailable.  Please try again later.", "SYS-0002", null);

		throw new WebApplicationException(Response.status(error.getCode()).entity(error)
				.type(MediaType.APPLICATION_JSON).type(ErrorUtils.getBestMediaType()).build());
	}
}
