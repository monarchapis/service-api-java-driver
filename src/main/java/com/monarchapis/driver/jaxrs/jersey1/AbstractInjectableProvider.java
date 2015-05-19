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

package com.monarchapis.driver.jaxrs.jersey1;

import java.lang.reflect.Type;

import com.monarchapis.driver.annotation.ApiInject;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

/**
 * Provides support for the @ApiInject annotation to inject various types into a
 * resource class instance.
 * 
 * @author Phil Kedy
 */
public abstract class AbstractInjectableProvider<E> extends AbstractHttpContextInjectable<E> implements
		InjectableProvider<ApiInject, Type> {

	/**
	 * The injected type.
	 */
	private final Type t;

	public AbstractInjectableProvider(Type t) {
		this.t = t;
	}

	@Override
	public Injectable<E> getInjectable(ComponentContext ic, ApiInject a, Type c) {
		if (c.equals(t)) {
			return getInjectable(ic, a);
		}

		return null;
	}

	public Injectable<E> getInjectable(ComponentContext ic, ApiInject a) {
		return this;
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}
}