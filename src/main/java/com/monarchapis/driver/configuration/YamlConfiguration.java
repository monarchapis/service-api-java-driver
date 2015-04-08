package com.monarchapis.driver.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlConfiguration extends JsonConfiguration {
	protected ObjectMapper getObjectMapper() {
		return new ObjectMapper(new YAMLFactory());
	}
}
