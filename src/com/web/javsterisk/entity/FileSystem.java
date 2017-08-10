package com.web.javsterisk.entity;

public class FileSystem {
	
	private String name;
	private double total;
	private double used;
	private double free;
	private double usePct;
	
	public FileSystem() {
		super();
	}
	public FileSystem(String name, double total, double used, double free, double usePct) {
		super();
		this.name = name;
		this.total = total;
		this.used = used;
		this.free = free;
		this.usePct = usePct;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getUsed() {
		return used;
	}
	public void setUsed(double used) {
		this.used = used;
	}
	public double getFree() {
		return free;
	}
	public void setFree(double free) {
		this.free = free;
	}
	public double getUsePct() {
		return usePct;
	}
	public void setUsePct(double usePct) {
		this.usePct = usePct;
	}
	@Override
	public String toString() {
		return "FileSystem [name=" + name + ", total=" + total + ", used=" + used + ", free=" + free + ", usePct="
				+ usePct + "]";
	}
	
}
