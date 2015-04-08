package com.monarchapis.driver.model;

public class Reference {
	private String id;
	private String name;

	public Reference() {
	}

	public Reference(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name + " = " + id;
	}
}
