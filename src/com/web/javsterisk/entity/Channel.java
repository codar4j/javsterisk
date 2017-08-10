package com.web.javsterisk.entity;

public class Channel {
	
	private String extensionSource;
	
	private String extensionTarget;
	
	private String channel;
	
	private String location;
	
	private String state;
	
	private String applicationData;	
	
	private boolean source;
	
	public String getExtensionSource() {
		return extensionSource;
	}

	public void setExtensionSource(String extensionSource) {
		this.extensionSource = extensionSource;
	}

	public String getExtensionTarget() {
		return extensionTarget;
	}

	public void setExtensionTarget(String extensionTarget) {
		this.extensionTarget = extensionTarget;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApplicationData() {
		return applicationData;
	}

	public void setApplicationData(String applicationData) {
		this.applicationData = applicationData;
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "Channel [extensionSource=" + extensionSource
				+ ", extensionTarget=" + extensionTarget + ", channel="
				+ channel + ", location=" + location + ", state=" + state
				+ ", applicationData=" + applicationData + ", source=" + source
				+ "]";
	}
	
}
