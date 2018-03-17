package com.iugu.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Event {
	
	invoice_created("invoice.created"),
	invoice_status_changed("invoice.status_changed");
	
	private String value;

	private Event(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

}
