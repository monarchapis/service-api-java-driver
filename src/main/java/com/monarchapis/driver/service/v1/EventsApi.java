package com.monarchapis.driver.service.v1;

public interface EventsApi {
	public void sendEvent(String eventType, Object data);
}
