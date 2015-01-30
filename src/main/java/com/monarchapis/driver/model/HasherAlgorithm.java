package com.monarchapis.driver.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class HasherAlgorithm {
	private String name;
	private String[] algorithms;

	public HasherAlgorithm(String name, String[] algorithms) {
		this.name = name;
		this.algorithms = algorithms;
	}

	public String getName() {
		return name;
	}

	public String[] getAlgorithms() {
		return algorithms;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}