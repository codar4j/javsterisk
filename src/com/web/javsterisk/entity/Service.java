package com.web.javsterisk.entity;

import org.hyperic.sigar.ProcState;

public class Service {

	private long pid;
	
	private boolean running;
	
	private ProcState procState;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public ProcState getProcState() {
		return procState;
	}

	public void setProcState(ProcState procState) {
		this.procState = procState;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
}
