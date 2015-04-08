package com.monarchapis.driver.jaxrs.common;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.annotation.BypassAnalytics;
import com.monarchapis.driver.exception.NotFoundException;
import com.monarchapis.driver.model.OAuthEndpoints;
import com.monarchapis.driver.util.ServiceResolver;

@Path("/{version}/api-docs")
@BypassAnalytics
public class SwaggerDocumentationResource {
	private static final Logger log = LoggerFactory.getLogger(SwaggerDocumentationResource.class);

	private static final String SWAGGER_DOC_ROOT = "/documentation/swagger/";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getResourceListing(@PathParam("version") String version, @Context UriInfo uriInfo) {
		try {
			InputStream is = getClass().getResourceAsStream(SWAGGER_DOC_ROOT + version + "/resource-listing.json");

			if (is == null) {
				throw new NotFoundException();
			}

			String json = IOUtils.toString(is);
			json = StringUtils.replace(json, "${apiBasePath}", getBaseUri(uriInfo));
			json = insertEndpoints(json);

			return json;
		} catch (Exception e) {
			log.error("Could not get swagger documentation.", e);
			throw new NotFoundException();
		}
	}

	@GET
	@Path("/{route: .+}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getApiDeclaration(@PathParam("version") String version, @PathParam("route") String route,
			@Context UriInfo uriInfo) {
		try {
			InputStream is = getClass().getResourceAsStream(SWAGGER_DOC_ROOT + version + "/" + route + ".json");

			if (is == null) {
				throw new NotFoundException();
			}

			String json = IOUtils.toString(is);
			json = StringUtils.replace(json, "${apiBasePath}", getBaseUri(uriInfo));
			json = insertEndpoints(json);

			return json;
		} catch (Exception e) {
			log.error("Could not get swagger documentation.", e);
			throw new NotFoundException();
		}
	}

	private static String getBaseUri(UriInfo uriInfo) {
		return StringUtils.removeEnd(uriInfo.getBaseUri().toString(), "/");
	}

	private static String insertEndpoints(String json) {
		OAuthEndpoints endpoints = ServiceResolver.getInstance().getService(OAuthEndpoints.class);

		json = StringUtils.replace(json, "${oauthPasswordUrl}", endpoints.getPasswordUrl());
		json = StringUtils.replace(json, "${oauthImplicitUrl}", endpoints.getImplicitUrl());
		json = StringUtils.replace(json, "${oauthAuthorizationCodeRequestUrl}",
				endpoints.getAuthorizationCodeRequestUrl());
		json = StringUtils.replace(json, "${oauthAuthorizationCodeTokenUrl}", endpoints.getAuthorizationCodeTokenUrl());

		return json;
	}
}
