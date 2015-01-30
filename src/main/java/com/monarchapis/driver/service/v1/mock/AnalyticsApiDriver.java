package com.monarchapis.driver.service.v1.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monarchapis.driver.service.v1.AnalyticsApi;

public class AnalyticsApiDriver implements AnalyticsApi {
	private static final Logger logger = LoggerFactory.getLogger(AnalyticsApiDriver.class);

	@Override
	public void event(String eventType, ObjectNode data) {
		logger.info("Analyics: measure {}", data);
	}
}
