package com.iugu.model;

import java.util.HashMap;



public class Trigger {
	

    private Event event;

    private HashMap<String,String> data = new HashMap<String, String>();

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
    
    
 

}
