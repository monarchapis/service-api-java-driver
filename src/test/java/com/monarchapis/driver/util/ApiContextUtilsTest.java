package com.monarchapis.driver.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.monarchapis.driver.model.ApiContext;
import com.monarchapis.driver.model.PrincipalContext;

public class ApiContextUtilsTest {
	private static ApiContext emptyContext;

	private static ApiContext fullContext;

	private static PrincipalContext principal;

	static {
		emptyContext = new ApiContext();

		Map<String, Set<String>> claims = new HashMap<String, Set<String>>();
		claims.put("key", Sets.newHashSet("value"));
		principal = new PrincipalContext();
		principal.setId("test principal id");
		principal.setClaims(claims);

		fullContext = new ApiContext();
		fullContext.setPrincipal(principal);
	}

	@BeforeClass
	public static void initialize() {
		ApiContext.setAutoCreate(false);
	}

	@AfterClass
	public static void destroy() {
		ApiContext.setAutoCreate(true);
	}

	@Before
	@After
	public void setup() {
		ApiContext.remove();
	}

	@Test
	public void testGetPrincipal() {
		assertNull(ApiContextUtils.getPrincipal());

		ApiContext.setCurrent(emptyContext);
		assertNull(ApiContextUtils.getPrincipal());

		ApiContext.setCurrent(fullContext);
		assertSame(principal, ApiContextUtils.getPrincipal());
	}

	@Test
	public void testGetUserId() {
		assertNull(ApiContextUtils.getUserId());

		ApiContext.setCurrent(emptyContext);
		assertNull(ApiContextUtils.getUserId());

		ApiContext.setCurrent(fullContext);
		assertSame("test principal id", ApiContextUtils.getUserId());
	}

	@Test
	public void testHasClaim() {
		assertFalse(ApiContextUtils.hasClaim("key"));
		assertFalse(ApiContextUtils.hasClaim("key", "value"));

		ApiContext.setCurrent(emptyContext);
		assertFalse(ApiContextUtils.hasClaim("key"));
		assertFalse(ApiContextUtils.hasClaim("key", "value"));

		ApiContext.setCurrent(fullContext);
		assertTrue(ApiContextUtils.hasClaim("key"));
		assertTrue(ApiContextUtils.hasClaim("key", "value"));
	}
}
