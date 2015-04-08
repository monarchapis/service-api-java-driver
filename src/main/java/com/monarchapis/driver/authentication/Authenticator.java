package com.monarchapis.driver.authentication;

import java.math.BigDecimal;

import com.monarchapis.driver.annotation.Claim;

public interface Authenticator {
	public static final BigDecimal NORMAL_WEIGHT = new BigDecimal("1");

	public void performAccessChecks(BigDecimal requestWeight, String[] client, String[] delegated, boolean user,
			Claim[] claims);
}
