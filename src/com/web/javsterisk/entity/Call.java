package com.web.javsterisk.entity;

public class Call {
	
	private int id;
	
	private Channel source;
	
	private Channel target;
	
	private boolean active;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Channel getSource() {
		return source;
	}

	public void setSource(Channel source) {
		this.source = source;
	}

	public Channel getTarget() {
		return target;
	}

	public void setTarget(Channel target) {
		this.target = target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Call [id=" + id + ", source=" + source + ", target=" + target
				+ ", active=" + active + "]";
	}
		
}
