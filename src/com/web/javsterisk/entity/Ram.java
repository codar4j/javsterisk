package com.web.javsterisk.entity;

public class Ram {

	private long total;
	private String usedPercent;
	private long used;
	private long free;
	private String freePercent;
	private long ram;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	public long getFree() {
		return free;
	}
	public void setFree(long free) {
		this.free = free;
	}
	public String getUsedPercent() {
		return usedPercent;
	}
	public void setUsedPercent(String usedPercent) {
		this.usedPercent = usedPercent;
	}
	public String getFreePercent() {
		return freePercent;
	}
	public void setFreePercent(String freePercent) {
		this.freePercent = freePercent;
	}
	public long getRam() {
		return ram;
	}
	public void setRam(long ram) {
		this.ram = ram;
	}
	
}
