package com.monarchapis.driver.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Optional;

public class SpringServiceResolver extends ServiceResolver implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		ServiceResolver.setInstance(this);
	}

	@PreDestroy
	public void destroy() {
		ServiceResolver.setInstance(null);
	}

	@Override
	public <T> T getService(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public <T> Optional<T> getOptional(Class<T> clazz) {
		try {
			return Optional.of(applicationContext.getBean(clazz));
		} catch (BeansException be) {
			return Optional.absent();
		}
	}
}