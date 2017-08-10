package com.web.javsterisk.entity;

public class CpuInfo {
	
	private String vendor;
	private String model;
	private int mhz;
	private int totalCpu;
	private int physicalCpu;
	private int coresCpu;
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getMhz() {
		return mhz;
	}
	public void setMhz(int mhz) {
		this.mhz = mhz;
	}
	public int getTotalCpu() {
		return totalCpu;
	}
	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}
	public int getPhysicalCpu() {
		return physicalCpu;
	}
	public void setPhysicalCpu(int physicalCpu) {
		this.physicalCpu = physicalCpu;
	}
	public int getCoresCpu() {
		return coresCpu;
	}
	public void setCoresCpu(int coresCpu) {
		this.coresCpu = coresCpu;
	}
	
}
