package com.monarchapis.driver.service.v1.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.service.v1.EventsApi;

public class EventsApiDriver implements EventsApi {
	private static final Logger logger = LoggerFactory.getLogger(EventsApiDriver.class);

	@Override
	public void sendEvent(String eventType, Object data) {
		logger.info("Event: {} {}", eventType, data);
	}
}
