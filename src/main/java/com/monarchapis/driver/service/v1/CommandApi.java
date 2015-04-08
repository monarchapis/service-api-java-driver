package com.monarchapis.driver.service.v1;

public interface CommandApi {
	public void sendCommand(String commandType, Object data);
}
