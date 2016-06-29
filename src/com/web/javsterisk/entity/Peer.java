package com.web.javsterisk.entity;

public class Peer {
	
	private int id;

	private String name;
	
	private String host;
	
	private String dyn;
	
	private String forceport;	
	
	private String ACL;
	
	private String port;
	
	private String status;
	
	private String realtime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDyn() {
		return dyn;
	}

	public void setDyn(String dyn) {
		this.dyn = dyn;
	}

	public String getForceport() {
		return forceport;
	}

	public void setForceport(String forceport) {
		this.forceport = forceport;
	}

	public String getACL() {
		return ACL;
	}

	public void setACL(String aCL) {
		ACL = aCL;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRealtime() {
		return realtime;
	}

	public void setRealtime(String realtime) {
		this.realtime = realtime;
	}

	@Override
	public String toString() {
		return "Peer [id=" + id + ", name=" + name + ", host=" + host
				+ ", dyn=" + dyn + ", forceport=" + forceport + ", ACL=" + ACL
				+ ", port=" + port + ", status=" + status + ", realtime="
				+ realtime + "]";
	}
	
}
