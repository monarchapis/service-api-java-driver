package com.monarchapis.driver.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StatMeasures {
	private String statType;
	private Map<String, String> dimensions = new LinkedHashMap<String, String>();
	private Map<String, Map<String, Long>> increments = new LinkedHashMap<String, Map<String, Long>>();
	private Map<String, Long> measures = new LinkedHashMap<String, Long>();

	public StatMeasures(String statType) {
		this.statType = statType;
	}

	public StatMeasures(String statType, Map<String, String> dimensions, Map<String, Map<String, Long>> increments,
			Map<String, Long> measures) {
		this.statType = statType;
		this.dimensions = dimensions;
		this.increments = increments;
		this.measures = measures;
	}

	public StatMeasures dimension(String name, String value) {
		dimensions.put(name, value);

		return this;
	}

	public StatMeasures increment(String name, String value, Long by) {
		Map<String, Long> values = increments.get(name);

		if (values == null) {
			values = new LinkedHashMap<String, Long>();
			increments.put(name, values);
		}

		values.put(value, by);

		return this;
	}

	public StatMeasures measure(String name, Long value) {
		measures.put(name, value);

		return this;
	}

	public String getStatType() {
		return statType;
	}

	public Map<String, String> getDimensions() {
		return dimensions;
	}

	public Map<String, Map<String, Long>> getIncrements() {
		return increments;
	}

	public Map<String, Long> getMeasures() {
		return measures;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE) //
				.append("statType", statType) //
				.append("dimensions", dimensions) //
				.append("increments", increments) //
				.append("measures", measures).toString();
	}
}
