package com.monarchapis.driver.service.v1;

import java.util.List;
import java.util.Set;

import com.monarchapis.driver.model.AuthenticationRequest;
import com.monarchapis.driver.model.AuthenticationResponse;
import com.monarchapis.driver.model.AuthorizationDetails;
import com.monarchapis.driver.model.GrantedApplication;
import com.monarchapis.driver.model.LocaleInfo;
import com.monarchapis.driver.model.Message;
import com.monarchapis.driver.model.Token;
import com.monarchapis.driver.model.TokenRequest;

public interface ServiceApi {
	public AuthenticationResponse authenticate(AuthenticationRequest request);

	public AuthorizationDetails getAuthorizationDetails(String authorizationScheme, String apiKey, String callbackUri,
			Set<String> permissions);

	public boolean authenticateClient(String authorizationScheme, String apiKey, String sharedSecret);

	public Token getToken(String apiKey, String token, String callbackUri);

	public Token getTokenByRefresh(String apiKey, String refreshToken, String callbackUri);

	public Token createToken(TokenRequest token);

	public boolean revokeToken(String apiKey, String token, String callbackUri);

	public Token refreshToken(Token token);

	public List<Message> getPermissionMessages(List<LocaleInfo> locales, Set<String> permissions);

	public List<GrantedApplication> getGrantedApplications(List<LocaleInfo> locales, String userId);

	public void revokeToken(String tokenId);
}
