package com.web.javsterisk.enumeration;

public enum SipType {

	USER("user"), PEERS("peers"), FRIEND("friend");
	
	private String value;

	
	private SipType(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
