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

package com.monarchapis.driver.jaxrs.jersey2;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import com.monarchapis.driver.annotation.ApiInject;

/**
 * Handles the binding of the @ApiInject annotation to various types.
 * 
 * @author Phil Kedy
 */
public class ApiInjectionBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(ApiValueFactoryProvider.class) //
				.to(ValueFactoryProvider.class) //
				.in(Singleton.class);

		bind(ApiValueFactoryProvider.InjectionResolver.class) //
				.to(new TypeLiteral<InjectionResolver<ApiInject>>() {
				}).in(Singleton.class);
	}
}