package com.web.javsterisk.entity;

public class FSystem {
	
	private String devName;
	private long total;
	private long used;
	private long avail;
	private long usePct;
	private String dirName;
	private String typeName;
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
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
	public long getAvail() {
		return avail;
	}
	public void setAvail(long avail) {
		this.avail = avail;
	}
	public long getUsePct() {
		return usePct;
	}
	public void setUsePct(long usePct) {
		this.usePct = usePct;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
