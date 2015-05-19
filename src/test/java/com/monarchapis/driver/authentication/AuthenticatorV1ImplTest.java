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

//import static org.junit.Assert.*;
//import static org.mockito.Matchers.*;
//import static org.mockito.Mockito.*;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.common.collect.Lists;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;
//
//import com.google.common.collect.Sets;
//import com.monarchapis.driver.annotation.Claim;
//import com.monarchapis.driver.exception.ApiError;
//import com.monarchapis.driver.exception.ApiErrorException;
//import com.monarchapis.driver.exception.ApiErrorFactory;
//import com.monarchapis.driver.hash.RequestHasher;
//import com.monarchapis.driver.hash.RequestHasherRegistry;
//import com.monarchapis.driver.model.ApiContext;
//import com.monarchapis.driver.model.AuthenticationRequest;
//import com.monarchapis.driver.model.AuthenticationResponse;
//import com.monarchapis.driver.model.ClientContext;
//import com.monarchapis.driver.model.HasherAlgorithm;
//import com.monarchapis.driver.model.HttpHeader;
//import com.monarchapis.driver.model.HttpResponseHolder;
//import com.monarchapis.driver.model.PrincipalContext;
//import com.monarchapis.driver.model.TokenContext;
//import com.monarchapis.driver.service.v1.ServiceApi;
//import com.monarchapis.driver.servlet.ApiRequest;
//
//@RunWith(MockitoJUnitRunner.class)
//public class AuthenticatorV1ImplTest {
//	@Mock
//	private ServiceApi serviceApi;
//
//	@Mock
//	private HasherAlgorithm hasherAlgorithm;
//
//	@Mock
//	private RequestHasher requestHasher;
//
//	@Mock
//	private RequestHasherRegistry requestHasherRegistry;
//
//	@Mock
//	private ApiErrorFactory apiErrorFactory;
//
//	@InjectMocks
//	private AuthenticatorV1Impl authenticator;
//
//	@Mock
//	private ApiRequest request;
//
//	private ApiContext apiContext;
//
//	private AuthenticationRequest authRequest;
//
//	@Mock
//	private HttpServletResponse response;
//
//	private BigDecimal requestWeight = new BigDecimal("1");
//
//	@Before
//	public void setup() {
//		authenticator.setHasherAlgorithms(Lists.newArrayList(new HasherAlgorithm("test", new String[] { "sha256" })));
//		when(requestHasherRegistry.getRequestHasher("test")).thenReturn(requestHasher);
//		when(requestHasher.getRequestHash(any(ApiRequest.class), anyString())).thenReturn("test");
//
//		when(apiErrorFactory.exception(anyString())).thenAnswer(new Answer<ApiErrorException>() {
//			@Override
//			public ApiErrorException answer(InvocationOnMock invocation) throws Throwable {
//				ApiError error = new ApiError();
//				error.setStatus(500);
//				error.setMessage("test");
//				error.setDeveloperMessage("test");
//				error.setReason(invocation.getArgumentAt(0, String.class));
//				error.setErrorCode("test-001");
//				error.setMoreInfo("http://developer.company.com/errors/test-001");
//				return new ApiErrorException(error);
//			}
//		});
//
//		ApiRequest.setCurrent(request);
//		HttpResponseHolder.setCurrent(response);
//
//		authRequest = new AuthenticationRequest();
//		when(request.createAuthorizationRequest()).thenReturn(authRequest);
//
//		ApiContext.remove();
//		apiContext = new ApiContext();
//		AuthenticationResponse authResponse = new AuthenticationResponse();
//		authResponse.setCode(200);
//		authResponse.setResponseHeaders(Lists.newArrayList(new HttpHeader("name", "value")));
//		authResponse.setContext(apiContext);
//		when(serviceApi.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);
//	}
//
//	@Test
//	public void testThrowForbiddenIfApiContextIsNull() {
//		AuthenticationResponse authResponse = new AuthenticationResponse();
//		authResponse.setCode(200);
//		when(serviceApi.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);
//
//		String[] client = new String[] { "test1" };
//		String[] delegated = new String[] {};
//		boolean user = false;
//		Claim[] claims = new Claim[] {};
//
//		try {
//			authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//			fail("An exception should have been thrown");
//		} catch (ApiErrorException apie) {
//			assertEquals("forbidden", apie.getError().getReason());
//		}
//	}
//
//	@Test
//	public void testHeadersReturnedFromAuthenticationResponse() {
//		String[] client = new String[] {};
//		String[] delegated = new String[] {};
//		boolean user = false;
//		Claim[] claims = new Claim[] {};
//
//		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//
//		verify(response).addHeader("name", "value");
//	}
//
//	@Test
//	public void testCalculateHashes() {
//		String[] client = new String[] {};
//		String[] delegated = new String[] {};
//		boolean user = false;
//		Claim[] claims = new Claim[] {};
//
//		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//
//		Map<String, Map<String, String>> hashes = authRequest.getPayloadHashes();
//		assertNotNull(hashes);
//		Map<String, String> hash = hashes.get("test");
//		assertEquals("test", hash.get("sha256"));
//	}
//
//	@Test
//	public void testCheckApiContextExists() {
//	}
//
//	@Test
//	public void testCheckClientPermissions() {
//		ClientContext clientContext = new ClientContext();
//		clientContext.setId("test");
//		clientContext.setLabel("label");
//		clientContext.setPermissions(Sets.newHashSet("fail"));
//
//		apiContext.setClient(clientContext);
//		String[] client = new String[] { "test1" };
//		String[] delegated = new String[] {};
//		boolean user = false;
//		Claim[] claims = new Claim[] {};
//
//		try {
//			authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//			fail("An exception should have been thrown");
//		} catch (ApiErrorException apie) {
//			assertEquals("forbidden", apie.getError().getReason());
//		}
//
//		clientContext.setPermissions(Sets.newHashSet("test1"));
//
//		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//	}
//
//	@Test
//	public void testCheckDelegatedPermissions() {
//		TokenContext tokenContext = new TokenContext();
//		tokenContext.setId("test");
//		tokenContext.setPermissions(Sets.newHashSet("fail"));
//
//		apiContext.setToken(tokenContext);
//		String[] client = new String[] {};
//		String[] delegated = new String[] { "test1" };
//		boolean user = false;
//		Claim[] claims = new Claim[] {};
//
//		try {
//			authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//			fail("An exception should have been thrown");
//		} catch (ApiErrorException apie) {
//			assertEquals("forbidden", apie.getError().getReason());
//		}
//
//		tokenContext.setPermissions(Sets.newHashSet("test1"));
//
//		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//	}
//
//	@Test
//	public void testCheckClaims() {
//		String[] client = new String[] {};
//		String[] delegated = new String[] {};
//		boolean user = false;
//		Claim claim = mock(Claim.class);
//		when(claim.type()).thenReturn("type");
//		when(claim.value()).thenReturn("value");
//		Claim[] claims = new Claim[] { claim };
//
//		try {
//			authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//			fail("An exception should have been thrown");
//		} catch (ApiErrorException apie) {
//			assertEquals("invalidAccessToken", apie.getError().getReason());
//		}
//
//		PrincipalContext principalContext = new PrincipalContext();
//		principalContext.setId("test");
//		Map<String, Set<String>> claimsMap = new HashMap<String, Set<String>>();
//		// claimsMap.put("type", Sets.newHashSet("value"));
//		principalContext.setClaims(claimsMap);
//		apiContext.setPrincipal(principalContext);
//
//		try {
//			authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//			fail("An exception should have been thrown");
//		} catch (ApiErrorException apie) {
//			assertEquals("forbidden", apie.getError().getReason());
//		}
//
//		claimsMap.put("type", Sets.newHashSet("value"));
//
//		authenticator.performAccessChecks(requestWeight, client, delegated, user, claims);
//	}
//}
