package com.monarchapis.driver.jaxrs.v2;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.CommonProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

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
