package com.monarchapis.driver.model;

public class ServiceInfo {
	private Reference environment;
	private Reference service;
	private Reference provider;

	public Reference getEnvironment() {
		return environment;
	}

	public void setEnvironment(Reference environment) {
		this.environment = environment;
	}

	public Reference getService() {
		return service;
	}

	public void setService(Reference service) {
		this.service = service;
	}

	public Reference getProvider() {
		return provider;
	}

	public void setProvider(Reference provider) {
		this.provider = provider;
	}

	public String toString() {
		return "environment: " + environment + " / service: " + service + " / provider: " + provider;
	}
}
