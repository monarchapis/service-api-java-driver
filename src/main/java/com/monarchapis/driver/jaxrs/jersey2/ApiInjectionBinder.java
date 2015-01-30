package com.monarchapis.driver.jaxrs.jersey2;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import com.monarchapis.driver.annotation.ApiInject;

public class ApiInjectionBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(ApiValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
		bind(ApiValueFactoryProvider.InjectionResolver.class).to(new TypeLiteral<InjectionResolver<ApiInject>>() {
		}).in(Singleton.class);
	}
}