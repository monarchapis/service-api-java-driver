package com.monarchapis.driver.analytics;

import com.monarchapis.driver.servlet.ApiRequest;
import com.monarchapis.driver.servlet.ApiResponse;

public interface AnalyticsHandler {
	void collect(ApiRequest request, ApiResponse response, long ms);
}
