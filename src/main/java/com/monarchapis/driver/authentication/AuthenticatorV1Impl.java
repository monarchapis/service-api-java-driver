package com.monarchapis.driver.authentication;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.annotation.Claim;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.hash.RequestHasher;
import com.monarchapis.driver.hash.RequestHasherRegistry;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.HasherAlgorithm;
import com.monarchapis.driver.model.HttpHeader;
import com.monarchapis.driver.model.HttpResponseHolder;
import com.monarchapis.driver.service.v1.ServiceApi;
import com.monarchapis.driver.servlet.ApiRequest;

@Named
public class AuthenticatorV1Impl implements Authenticator {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticatorV1Impl.class);

	@Inject
	private ServiceApi serviceApi;

	@Inject
	private List<HasherAlgorithm> hasherAlgorithms;

	@Inject
	private RequestHasherRegistry requestHasherRegistry;

	@Inject
	private ApiErrorFactory apiErrorFactory;

	@Override
	public void performAccessChecks(BigDecimal requestWeight, String[] client, String[] delegated, boolean user,
			Claim[] claims) {
		ApiContext apiContext = getApiContext(requestWeight);

		if (apiContext == null) {
			throw apiErrorFactory.exception("forbidden");
		}

		for (String permission : client) {
			if (!apiContext.hasClientPermission(permission)) {
				throw apiErrorFactory.exception("forbidden");
			}
		}

		// Only check delegated permissions if a token is part of the API
		// context.
		if (delegated.length > 0 && apiContext.getToken() != null) {
			for (String permission : delegated) {
				if (!apiContext.hasDelegatedPermission(permission)) {
					throw apiErrorFactory.exception("forbidden");
				}
			}
		}

		if (user || claims.length > 0) {
			if (apiContext.getPrincipal() == null) {
				throw apiErrorFactory.exception("invalidAccessToken");
			}

			for (Claim claim : claims) {
				if (!apiContext.hasUserClaim(claim.type(), claim.value())) {
					throw apiErrorFactory.exception("forbidden");
				}
			}
		}
	}

	private ApiContext getApiContext(BigDecimal requestWeight) {
		ApiContext apiContext = ApiContext.getCurrent();

		if (apiContext.getClient() == null) {
			AuthenticationResponse authResponse = null;

			try {
				authResponse = processAuthentication(requestWeight);
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
				// Map the exception based on the error reason
				throw apiErrorFactory.exception(authResponse.getReason());
			}
		}

		return apiContext;
	}

	private AuthenticationResponse processAuthentication(BigDecimal requestWeight) {
		ApiRequest apiRequest = ApiRequest.getCurrent();
		HttpServletResponse httpResponse = HttpResponseHolder.getCurrent();

		AuthenticationRequest authRequest = prepareAuthenticationRequest(apiRequest);
		authRequest.setRequestWeight(requestWeight);
		AuthenticationResponse authResponse = serviceApi.authenticate(authRequest);

		if (authResponse.getResponseHeaders() != null) {
			for (HttpHeader header : authResponse.getResponseHeaders()) {
				httpResponse.addHeader(header.getName(), header.getValue());
			}
		}

		return authResponse;
	}

	private AuthenticationRequest prepareAuthenticationRequest(ApiRequest apiRequest) {
		AuthenticationRequest authRequest = apiRequest.createAuthorizationRequest();

		if (hasherAlgorithms != null) {
			for (HasherAlgorithm hasherAlgo : hasherAlgorithms) {
				RequestHasher hasher = requestHasherRegistry.getRequestHasher(hasherAlgo.getName());

				for (String algorithm : hasherAlgo.getAlgorithms()) {
					String hash = hasher.getRequestHash(apiRequest, algorithm);

					if (hash != null) {
						authRequest.setPayloadHash(hasherAlgo.getName(), algorithm, hash);
					}
				}
			}
		}

		return authRequest;
	}

	private void throwSystemException(Exception e) {
		logger.error("Could not authenticate API request", e);

		throw apiErrorFactory.exception("serviceUnavailable");
	}

	public void setServiceApi(ServiceApi serviceApi) {
		this.serviceApi = serviceApi;
	}

	public void setHasherAlgorithms(List<HasherAlgorithm> hasherAlgorithms) {
		this.hasherAlgorithms = hasherAlgorithms;
	}

	public void setRequestHasherRegistry(RequestHasherRegistry requestHasherRegistry) {
		this.requestHasherRegistry = requestHasherRegistry;
	}

	public void setApiErrorFactory(ApiErrorFactory apiErrorFactory) {
		this.apiErrorFactory = apiErrorFactory;
	}
}
