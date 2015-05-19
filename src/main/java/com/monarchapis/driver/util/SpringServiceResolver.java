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

package com.monarchapis.driver.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Optional;

/**
 * A Spring implementation of ServiceResolver that uses
 * <code>ApplicationContext</code> to look up beans.
 * 
 * @author Phil Kedy
 */
public class SpringServiceResolver extends ServiceResolver implements ApplicationContextAware {
	/**
	 * The application context.
	 */
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Initializes the service resolver singleton.
	 */
	@PostConstruct
	public void init() {
		ServiceResolver.setInstance(this);
	}

	/**
	 * Unsets the service resolver singleton.
	 */
	@PreDestroy
	public void destroy() {
		ServiceResolver.setInstance(null);
	}

	@Override
	public <T> T required(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public <T> Optional<T> optional(Class<T> clazz) {
		try {
			return Optional.of(applicationContext.getBean(clazz));
		} catch (BeansException be) {
			return Optional.absent();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getInstancesOf(Class<T> clazz) {
		String[] names = applicationContext.getBeanNamesForType(clazz);

		List<T> beans = new ArrayList<T>(names.length);

		for (String name : names) {
			beans.add((T) applicationContext.getBean(name));
		}

		return beans;
	}
}