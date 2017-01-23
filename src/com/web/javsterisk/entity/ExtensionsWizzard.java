package com.web.javsterisk.entity;

import java.util.List;

import javax.persistence.CascadeType;

// Generated 07-03-2014 02:28:25 PM by Hibernate Tools 3.4.0.CR1

//import javax.persistence.AttributeOverride;
//import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.IndexColumn;

/**
 * 
 * @author Freddy Moran
 *
 */
@Entity
@Table(name = "extensionsWizzard")
public class ExtensionsWizzard implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToMany(cascade= CascadeType.ALL)
	@JoinColumn(name="IdProfesor")
	private List<Extensions> extensions;
	
	@NotNull
	@Column(name = "digito", nullable = false)
	private String digito;
	
	@NotNull
	@Column(name = "longitud", nullable = false)
	private int longitud;
	
	@NotNull
	@Column(name = "record", nullable = false)
	private boolean record;
	
	@NotNull
	@Column(name = "limite", nullable = false)
	private boolean limit;
	
	@Column(name = "timeLimit")
	private String timeLimit;

	@Column(name = "firstAlert")
	private String firstAlert;
	
	@Column(name = "secondAlert")
	private String secondAlert;
	
	@NotNull
	@Column(name = "wait", nullable = false)
	private boolean wait;
	
	@Column(name = "timeWait")
	private String timeWait;
	
	@NotNull
	@Column(name = "transfer", nullable = false)
	private boolean transfer;
	
	@Column(name = "firstExtension")
	private String firstExtension;
	
	@Column(name = "secondExtension")
	private String secondExtension;

	public ExtensionsWizzard() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Extensions> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<Extensions> extensions) {
		this.extensions = extensions;
	}

	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	public int getLongitud() {
		return longitud;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public boolean isLimit() {
		return limit;
	}

	public void setLimit(boolean limit) {
		this.limit = limit;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getFirstAlert() {
		return firstAlert;
	}

	public void setFirstAlert(String firstAlert) {
		this.firstAlert = firstAlert;
	}

	public String getSecondAlert() {
		return secondAlert;
	}

	public void setSecondAlert(String secondAlert) {
		this.secondAlert = secondAlert;
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public String getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(String timeWait) {
		this.timeWait = timeWait;
	}

	public boolean isTransfer() {
		return transfer;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	public String getFirstExtension() {
		return firstExtension;
	}

	public void setFirstExtension(String firstExtension) {
		this.firstExtension = firstExtension;
	}

	public String getSecondExtension() {
		return secondExtension;
	}

	public void setSecondExtension(String secondExtension) {
		this.secondExtension = secondExtension;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExtensionsWizzard))
			return false;
		ExtensionsWizzard other = (ExtensionsWizzard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExtensionsWizzard [id=" + id + ", extensions=" + extensions + ", digito=" + digito + ", longitud="
				+ longitud + ", record=" + record + ", limit=" + limit + ", timeLimit=" + timeLimit + ", firstAlert="
				+ firstAlert + ", secondAlert=" + secondAlert + ", wait=" + wait + ", timeWait=" + timeWait
				+ ", transfer=" + transfer + ", firstExtension=" + firstExtension + ", secondExtension="
				+ secondExtension + "]";
	}
	
}
