package com.web.javsterisk.entity;

public class Cpu {

	private String userTime;
	private String sysTime;
	private String idleTime;
	private String combined;
	
	public String getUserTime() {
		return userTime;
	}
	public void setUserTime(String userTime) {
		this.userTime = userTime;
	}
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
	public String getIdleTime() {
		return idleTime;
	}
	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}
	public String getCombined() {
		return combined;
	}
	public void setCombined(String combined) {
		this.combined = combined;
	}
			
}
