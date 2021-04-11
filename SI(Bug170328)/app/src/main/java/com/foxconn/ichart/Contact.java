package com.foxconn.ichart;

public class Contact {
	private String name;
	private double value;
	private String color;


	public Contact(String name, double value, String color) {
		this.name = name;
		this.value = value;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
