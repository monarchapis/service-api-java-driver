package com.monarchapis.driver.jaxrs.jersey1;

import java.lang.reflect.Type;

import com.monarchapis.driver.annotation.ApiInject;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public abstract class AbstractInjectableProvider<E> extends AbstractHttpContextInjectable<E> implements
		InjectableProvider<ApiInject, Type> {

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