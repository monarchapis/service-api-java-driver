package com.monarchapis.driver.service.v1.impl;

import java.io.IOException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monarchapis.driver.service.v1.CommandApi;

public class CommandApiDriver extends AbstractEnvironmentDriver implements CommandApi {
	private static final Logger logger = LoggerFactory.getLogger(CommandApiDriver.class);

	@Override
	public void sendCommand(String eventType, Object data) {
		try {
			String url = "/events/v1/" + URLEncoder.encode(eventType, "UTF-8");
			executeJsonRequest(POST, url, data);
		} catch (IOException e) {
			logger.warn("An error occurred calling the event service", e);
		}
	}
}
