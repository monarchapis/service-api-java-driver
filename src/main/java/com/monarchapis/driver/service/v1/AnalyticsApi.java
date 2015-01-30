package com.monarchapis.driver.service.v1;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AnalyticsApi {
	public void event(String eventType, ObjectNode data);
}
