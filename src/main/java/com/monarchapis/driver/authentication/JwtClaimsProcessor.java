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

package com.monarchapis.driver.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JwtClaimsProcessor implements ClaimsProcessor {
	private static final Logger logger = LoggerFactory.getLogger(JwtClaimsProcessor.class);

	private static ObjectMapper MAPPER = new ObjectMapper();

	@Value("${jwt.key}")
	private String jwtKey;

	public ObjectNode getClaims(String jwt) {
		Claims body = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt).getBody();
		ObjectNode node = MAPPER.convertValue(body, ObjectNode.class);

		return node;
	}

	@Override
	public ObjectNode getClaims(HttpServletRequest request) {
		if (StringUtils.isBlank(jwtKey)) {
			return null;
		}

		String authorization = request.getHeader("Authorization");

		if (authorization != null && StringUtils.startsWithIgnoreCase(authorization, "Bearer ")) {
			String jwt = authorization.substring(7).trim();

			if (StringUtils.countMatches(jwt, ".") == 2) {
				try {
					return getClaims(jwt);
				} catch (Exception e) {
					logger.warn("Could not parse JWT token", e);
				}
			}
		}

		return null;
	}

	public void setJwtKey(String jwtKey) {
		this.jwtKey = jwtKey;
	}
}
