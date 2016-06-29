package com.web.javsterisk.entity;

import java.io.Serializable;

/**
 * 
 * @author atorres
 *
 */

public class LogAsterisk implements Serializable {
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String url;
	
	public LogAsterisk(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
