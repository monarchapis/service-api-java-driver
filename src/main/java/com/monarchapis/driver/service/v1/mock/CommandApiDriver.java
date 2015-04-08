package com.monarchapis.driver.service.v1.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.service.v1.CommandApi;

public class CommandApiDriver implements CommandApi {
	private static final Logger logger = LoggerFactory.getLogger(CommandApiDriver.class);

	@Override
	public void sendCommand(String eventType, Object data) {
		logger.info("Event: {} {}", eventType, data);
	}
}
