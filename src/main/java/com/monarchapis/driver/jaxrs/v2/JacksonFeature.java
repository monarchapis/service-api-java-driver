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

package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.CommonProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Disables Moxy and registers the necessary Jackson JSON provider.
 * 
 * @author Phil Kedy
 */
@Provider
public class JacksonFeature implements Feature {
	@Override
	public boolean configure(FeatureContext context) {
		String disableMoxy = CommonProperties.MOXY_JSON_FEATURE_DISABLE + '.'
				+ context.getConfiguration().getRuntimeType().name().toLowerCase();
		context.property(disableMoxy, true);

		context.register(JacksonJaxbJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);

		return true;
	}
}
