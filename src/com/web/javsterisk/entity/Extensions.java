package com.web.javsterisk.entity;

// Generated 07-03-2014 02:28:25 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Extensions generated by hbm2java
 */
@Entity
@Table(name = "extensions")
public class Extensions implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "context", column = @Column(name = "context", nullable = false, length = 20)),
			@AttributeOverride(name = "exten", column = @Column(name = "exten", nullable = false, length = 20)),
			@AttributeOverride(name = "priority", column = @Column(name = "priority", nullable = false)) })
	private ExtensionsId id;
		
	@NotNull
	@Column(name = "id", nullable = false)	
	private int id_1;

	@NotNull
	@Size(max = 20)	
	@Column(name = "app", nullable = false, length = 20)
	private String app = "";

	@Size(max = 128)
	@Column(name = "appdata", nullable = true, length = 128)
	private String appdata = "";

	public Extensions() {
	}

	public Extensions(ExtensionsId id, int id_1, String app, String appdata) {
		this.id = id;
		this.id_1 = id_1;
		this.app = app;
		this.appdata = appdata;
	}
	
	public ExtensionsId getId() {
		return this.id;
	}

	public void setId(ExtensionsId id) {
		this.id = id;
	}
	
	public int getId_1() {
		return this.id_1;
	}

	public void setId_1(int id_1) {
		this.id_1 = id_1;
	}
	
	public String getApp() {
		return this.app;
	}

	public void setApp(String app) {
		this.app = app;
	}
	
	public String getAppdata() {
		return this.appdata;
	}

	public void setAppdata(String appdata) {
		this.appdata = appdata;
	}

}
