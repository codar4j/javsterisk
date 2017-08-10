package com.web.javsterisk.enumeration;

public enum RoleType {

	ADMINISTRADOR("Administrador"), ESTANDAR("Estandar");
	
	private String value;

	
	private RoleType(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
