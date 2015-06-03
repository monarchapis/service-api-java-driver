package com.monarchapis.driver.spring;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Sets;
import com.monarchapis.api.v1.client.AnalyticsApi;
import com.monarchapis.api.v1.client.CommandApi;
import com.monarchapis.api.v1.client.ManagementApi;
import com.monarchapis.api.v1.client.OpenApi;
import com.monarchapis.api.v1.client.ServiceApi;
import com.monarchapis.api.v1.client.impl.AnalyticsApiImpl;
import com.monarchapis.api.v1.client.impl.CommandApiImpl;
import com.monarchapis.api.v1.client.impl.ManagementApiImpl;
import com.monarchapis.api.v1.client.impl.OpenApiImpl;
import com.monarchapis.api.v1.client.impl.ServiceApiImpl;
import com.monarchapis.client.authentication.HawkV1RequestProcessor;
import com.monarchapis.client.rest.RestClientFactory;
import com.monarchapis.driver.analytics.AnalyticsHandler;
import com.monarchapis.driver.analytics.MonarchV1AnalyticsHandler;
import com.monarchapis.driver.authentication.Authenticator;
import com.monarchapis.driver.authentication.AuthenticatorV1Impl;
import com.monarchapis.driver.authentication.EnvironmentRequestProcessor;
import com.monarchapis.driver.authentication.JwtClaimsProcessor;
import com.monarchapis.driver.configuration.ConfigurationBundle;
import com.monarchapis.driver.configuration.ResourceConfigurationBundle;
import com.monarchapis.driver.exception.ApiErrorFactory;
import com.monarchapis.driver.exception.ConfigurationBundleApiErrorFactory;
import com.monarchapis.driver.hash.HawkV1RequestHasher;
import com.monarchapis.driver.hash.RequestHasher;
import com.monarchapis.driver.hash.RequestHasherRegistry;
import com.monarchapis.driver.model.AuthenticationSettings;
import com.monarchapis.driver.model.HasherAlgorithm;
import com.monarchapis.driver.model.OAuthEndpoints;
import com.monarchapis.driver.service.v1.ServiceInfoResolver;
import com.monarchapis.driver.service.v1.SingleServiceInfoResolver;
import com.monarchapis.driver.servlet.ApiFilter;
import com.monarchapis.driver.servlet.CorsFilter;
import com.monarchapis.driver.util.ServiceResolver;
import com.monarchapis.driver.util.SpringServiceResolver;

@Configuration
public class MonarchConfigurer {
	@Value("${service.name}")
	private String serviceName;

	@Value("${monarch.environmentName}")
	private String environmentName;

	@Value("${monarch.providerKey}")
	private String providerKey;

	@Value("${monarch.sharedSecret}")
	private String sharedSecret;

	@Value("${jwt.key}")
	private String jwtKey;

	@Value("${monarch.url}/open/v1")
	private String openApiUrl;

	@Value("${monarch.url}/service/v1")
	private String serviceApiUrl;

	@Value("${monarch.url}/analytics/v1")
	private String analyticsApiUrl;

	@Value("${monarch.url}/command/v1")
	private String commandApiUrl;

	@Value("${service.delegateAuthorization}")
	private boolean delegateAuthorization;

	@Value("${service.bypassAnalytics}")
	private boolean bypassAnalytics;

	@Value("${service.bypassRateLimiting}")
	private boolean bypassRateLimiting;

	@Value("${error.moreInfoFormat}")
	private String moreInfoFormat;

	@Value("${oauth.authorizationCode.request.url}")
	private String oauthAuthorizationCodeRequestUrl;

	@Value("${oauth.authorizationCode.token.url}")
	private String oauthAuthorizationCodeTokenUrl;

	@Value("${oauth.implicit.url}")
	private String oauthImplicitUrl;

	@Value("${oauth.password.url}")
	private String oauthPasswordUrl;

	@Bean
	public ServiceResolver serviceResolver() {
		return new SpringServiceResolver();
	}

	@Bean
	public JwtClaimsProcessor jwtClaimsProcessor() {
		JwtClaimsProcessor processor = new JwtClaimsProcessor();
		processor.setJwtKey(jwtKey);

		return processor;
	}

	@Bean
	public ConfigurationBundle configurationBundle() throws IOException {
		return new ResourceConfigurationBundle("com/monarchapis/driver/Errors");
	}

	@Bean
	public ApiErrorFactory apiErrorFactory(ConfigurationBundle bundle) {
		ConfigurationBundleApiErrorFactory factory = new ConfigurationBundleApiErrorFactory();
		factory.setConfigurationBundle(bundle);
		factory.setMoreInfoFormat(moreInfoFormat);

		return factory;
	}

	@Bean
	public Filter corsFilter() {
		return new CorsFilter();
	}

	@Bean
	public Filter apiFilter() {
		return new ApiFilter();
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapper() {
		return new Jackson2ObjectMapperBuilder() //
				.modules(//
						new JodaModule(), //
						new GuavaModule()) //
				.dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
	}

	@Bean
	public ServiceInfoResolver serviceInfoResolver(OpenApi openApi) {
		return new SingleServiceInfoResolver(openApi, environmentName, serviceName, providerKey);
	}

	@Bean
	public EnvironmentRequestProcessor environmentRequestProcessor(ServiceInfoResolver serviceInfoResolver) {
		return new EnvironmentRequestProcessor(serviceInfoResolver);
	}

	@Bean
	public HawkV1RequestProcessor hawkV1RequestProcessor() {
		return new HawkV1RequestProcessor(providerKey, sharedSecret, "sha256");
	}

	@Bean
	public RequestHasher hawkV1RequestHasher() {
		return new HawkV1RequestHasher();
	}

	@Bean
	public HasherAlgorithm hawkV1HasherAlgorithm() {
		return new HasherAlgorithm("Hawk V1", new String[] { "sha256" });
	}

	@Bean
	public RequestHasherRegistry requestHasherRegistry(List<RequestHasher> requestHashers) {
		return new RequestHasherRegistry(requestHashers);
	}

	@Bean
	public AuthenticationSettings authenticationSettings() {
		AuthenticationSettings settings = new AuthenticationSettings();

		settings.setDelegateAuthorization(delegateAuthorization);
		settings.setBypassRateLimiting(bypassRateLimiting);
		settings.setBypassAnalytics(bypassAnalytics);

		return settings;
	}

	@Bean
	public Authenticator authenticator(List<HasherAlgorithm> hasherAlgorithms,
			RequestHasherRegistry requestHasherRegistry, ServiceApi serviceApi, ApiErrorFactory apiErrorFactory) {
		AuthenticatorV1Impl authenticator = new AuthenticatorV1Impl();
		authenticator.setHasherAlgorithms(hasherAlgorithms);
		authenticator.setRequestHasherRegistry(requestHasherRegistry);
		authenticator.setServiceApi(serviceApi);
		authenticator.setApiErrorFactory(apiErrorFactory);

		return authenticator;
	}

	@Bean
	public OAuthEndpoints oauthEndpoints() {
		OAuthEndpoints endpoints = new OAuthEndpoints();
		endpoints.setAuthorizationCodeRequestUrl(oauthAuthorizationCodeRequestUrl);
		endpoints.setAuthorizationCodeTokenUrl(oauthAuthorizationCodeTokenUrl);
		endpoints.setImplicitUrl(oauthImplicitUrl);
		endpoints.setPasswordUrl(oauthPasswordUrl);

		return endpoints;
	}

	@Bean
	public RestClientFactory restClientFactory() {
		return new RestClientFactory();
	}

	@Bean
	public OpenApi openApi(RestClientFactory restClientFactory) {
		return new OpenApiImpl(openApiUrl, restClientFactory);
	}

	@Bean
	public ServiceApi serviceApi(RestClientFactory restClientFactory, EnvironmentRequestProcessor eventProcessor,
			HawkV1RequestProcessor hawkV1RequestSigner) {
		return new ServiceApiImpl(serviceApiUrl, restClientFactory, eventProcessor, hawkV1RequestSigner);
	}

	@Bean
	public ManagementApi managementApi(RestClientFactory restClientFactory, EnvironmentRequestProcessor eventProcessor,
			HawkV1RequestProcessor hawkV1RequestSigner) {
		return new ManagementApiImpl(commandApiUrl, restClientFactory, eventProcessor, hawkV1RequestSigner);
	}

	@Bean
	public AnalyticsApi analyticsApi(RestClientFactory restClientFactory, EnvironmentRequestProcessor eventProcessor,
			HawkV1RequestProcessor hawkV1RequestSigner) {
		return new AnalyticsApiImpl(analyticsApiUrl, restClientFactory, eventProcessor, hawkV1RequestSigner);
	}

	@Bean
	public CommandApi commandApi(RestClientFactory restClientFactory, EnvironmentRequestProcessor eventProcessor,
			HawkV1RequestProcessor hawkV1RequestSigner) {
		return new CommandApiImpl(commandApiUrl, restClientFactory, eventProcessor, hawkV1RequestSigner);
	}

	@Bean
	public AnalyticsHandler analyticsHandler(AnalyticsApi analyticsApi, ServiceInfoResolver serviceInfoResolver) {
		MonarchV1AnalyticsHandler handler = new MonarchV1AnalyticsHandler(analyticsApi, serviceInfoResolver);
		handler.setIgnoreUriPatterns(Sets.newHashSet(//
				"^/info$", //
				"^/env$", //
				"^/env/.*$", //
				"^/shutdown$", //
				"^/health$", //
				"^/beans$", //
				"^/dump$", //
				"^/mappings$", //
				"^/metrics$", //
				"^/metrics/.*$", //
				"^/configprops$", //
				"^/autoconfig$", //
				"^/trace$"));

		return handler;
	}
}
