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

/**
 * A resource class that serves Swagger 1.2 API documentation.
 * 
 * @author Phil Kedy
 */
@Path("/{version}/api-docs")
@BypassAnalytics
public class Swagger12DocumentationResource {
	private static final Logger log = LoggerFactory.getLogger(Swagger12DocumentationResource.class);

	private static final String SWAGGER_DOC_ROOT = "/documentation/swagger/";

	/**
	 * Attempts to load the pre-generated resource listings file, set the base
	 * URI, and return the JSON as a string.
	 * 
	 * @param version
	 *            The version from the path
	 * @param uriInfo
	 *            The URI info for determining the base URI
	 * @return the resource listing JSON.
	 */
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

	/**
	 * Returns the API declaration for a specific route/resources.
	 * 
	 * @param version
	 *            The version from the path
	 * @param route
	 *            The route/resource
	 * @param uriInfo
	 *            The URI info for determining the base URI
	 * @return the API declaration JSON.
	 */
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

	/**
	 * Returns the base URI from the <code>UriInfo</code>.
	 * 
	 * @param uriInfo
	 *            The URI info for determining the base URI
	 * @return the base URI.
	 */
	private static String getBaseUri(UriInfo uriInfo) {
		return StringUtils.removeEnd(uriInfo.getBaseUri().toString(), "/");
	}

	/**
	 * Inserts the OAuth endpoints into the JSON place holders.
	 * 
	 * @param json
	 *            The JSON to alter
	 * @return the new JSON with the OAuth endpoint place holders replaced.
	 */
	private static String insertEndpoints(String json) {
		OAuthEndpoints endpoints = ServiceResolver.getInstance().required(OAuthEndpoints.class);

		json = StringUtils.replace(json, "${oauthPasswordUrl}", endpoints.getPasswordUrl());
		json = StringUtils.replace(json, "${oauthImplicitUrl}", endpoints.getImplicitUrl());
		json = StringUtils.replace(json, "${oauthAuthorizationCodeRequestUrl}",
				endpoints.getAuthorizationCodeRequestUrl());
		json = StringUtils.replace(json, "${oauthAuthorizationCodeTokenUrl}", endpoints.getAuthorizationCodeTokenUrl());

		return json;
	}
}
