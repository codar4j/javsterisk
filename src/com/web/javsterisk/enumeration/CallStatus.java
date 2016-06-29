package com.web.javsterisk.enumeration;

public enum CallStatus {

	ANSWERED("ANSWERED"), NOANSWER("NO ANSWER");
	
	private String value;

	
	private CallStatus(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
