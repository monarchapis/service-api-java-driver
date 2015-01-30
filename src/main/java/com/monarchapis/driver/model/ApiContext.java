package com.monarchapis.driver.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApiContext {
	private static InheritableThreadLocal<ApiContext> current = new InheritableThreadLocal<ApiContext>();

	private String correlationId;
	private ApplicationContext application;
	private ClientContext client;
	private TokenContext token;
	private PrincipalContext principal;
	private ProviderContext provider;

	public void copy(ApiContext from) {
		this.correlationId = from.correlationId;
		this.application = from.application;
		this.client = from.client;
		this.token = from.token;
		this.principal = from.principal;
		this.provider = from.provider;
	}

	public static ApiContext getCurrent() {
		ApiContext ret = current.get();

		if (ret == null) {
			ret = new ApiContext();
			current.set(ret);
		}

		return ret;
	}

	public static void setCurrent(ApiContext context) {
		if (context != null)
			current.set(context);
		else
			current.remove();
	}

	public static void remove() {
		current.remove();
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public ApplicationContext getApplication() {
		return application;
	}

	public void setApplication(ApplicationContext application) {
		this.application = application;
	}

	public ClientContext getClient() {
		return client;
	}

	public void setClient(ClientContext client) {
		this.client = client;
	}

	public TokenContext getToken() {
		return token;
	}

	public void setToken(TokenContext token) {
		this.token = token;
	}

	public PrincipalContext getPrincipal() {
		return principal;
	}

	public void setPrincipal(PrincipalContext principal) {
		this.principal = principal;
	}

	public ProviderContext getProvider() {
		return provider;
	}

	public void setProvider(ProviderContext provider) {
		this.provider = provider;
	}

	public boolean hasClientPermission(String permission) {
		return (client != null) ? client.hasPermission(permission) : false;
	}

	public boolean hasDelegatedPermission(String permission) {
		return (token != null) ? token.hasPermission(permission) : false;
	}

	public boolean hasUserClaim(String type) {
		return (principal != null) ? principal.hasClaim(type) : false;
	}

	public boolean hasUserClaim(String type, String value) {
		return (principal != null) ? principal.hasClaim(type, value) : false;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
