package com.monarchapis.driver.service.v1;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.monarchapis.driver.hash.RequestHasherRegistry;
import com.monarchapis.driver.model.HasherAlgorithm;

public class ServiceContainer {
	private static ServiceContainer instance;

	@Inject
	private ServiceInfoResolver serviceInfoResolver;

	@Inject
	private ServiceApi serviceApi;

	@Inject
	private AnalyticsApi analyticsApi;

	@Inject
	private EventsApi eventsApi;

	@Inject
	private RequestHasherRegistry requestHasherRegistry = new RequestHasherRegistry();

	@Inject
	private List<HasherAlgorithm> hasherAlgorithms = new ArrayList<HasherAlgorithm>(0);

	@Inject
	private OAuthEndpoints oauthEndpoints;

	private boolean delegateAuthorization = false;

	private boolean captureAnalytics = true;

	private boolean bypassRateLimiting = false;

	public static ServiceContainer getInstance() {
		if (instance == null) {
			instance = new ServiceContainer();
		}

		return instance;
	}

	public static void destroy() {
		instance = null;
	}

	public ServiceContainer() {
		ServiceContainer.instance = this;
	}

	@PreDestroy
	public void dispose() {
		ServiceContainer.instance = null;
	}

	public ServiceInfoResolver getServiceInfoResolver() {
		return serviceInfoResolver;
	}

	public void setServiceInfoResolver(ServiceInfoResolver serviceInfoResolver) {
		this.serviceInfoResolver = serviceInfoResolver;
	}

	public ServiceApi getServiceApi() {
		return serviceApi;
	}

	public void setServiceApi(ServiceApi serviceApi) {
		this.serviceApi = serviceApi;
	}

	public AnalyticsApi getAnalyticsApi() {
		return analyticsApi;
	}

	public void setAnalyticsApi(AnalyticsApi analyticsApi) {
		this.analyticsApi = analyticsApi;
	}

	public EventsApi getEventsApi() {
		return eventsApi;
	}

	public void setEventsApi(EventsApi eventsApi) {
		this.eventsApi = eventsApi;
	}

	public RequestHasherRegistry getRequestHasherRegistry() {
		return requestHasherRegistry;
	}

	public void setRequestHasherRegistry(RequestHasherRegistry requestHasherRegistry) {
		this.requestHasherRegistry = requestHasherRegistry;
	}

	public List<HasherAlgorithm> getHasherAlgorithms() {
		return hasherAlgorithms;
	}

	public void setHasherAlgorithms(List<HasherAlgorithm> hasherAlgorithms) {
		this.hasherAlgorithms = hasherAlgorithms;
	}

	public OAuthEndpoints getOAuthEndpoints() {
		return oauthEndpoints;
	}

	public void setOauthEndpoints(OAuthEndpoints oauthEndpoints) {
		this.oauthEndpoints = oauthEndpoints;
	}

	public boolean isDelegateAuthorization() {
		return delegateAuthorization;
	}

	public void setDelegateAuthorization(boolean centralizedAuthorization) {
		this.delegateAuthorization = centralizedAuthorization;
	}

	public boolean isCaptureAnalytics() {
		return captureAnalytics;
	}

	public void setCaptureAnalytics(boolean captureAnalytics) {
		this.captureAnalytics = captureAnalytics;
	}

	public boolean isBypassRateLimiting() {
		return bypassRateLimiting;
	}

	public void setBypassRateLimiting(boolean bypassRateLimiting) {
		this.bypassRateLimiting = bypassRateLimiting;
	}
}
