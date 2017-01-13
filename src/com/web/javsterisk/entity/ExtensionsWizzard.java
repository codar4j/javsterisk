package com.web.javsterisk.entity;

// Generated 07-03-2014 02:28:25 PM by Hibernate Tools 3.4.0.CR1

//import javax.persistence.AttributeOverride;
//import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
	
	@OneToOne(fetch = FetchType.LAZY)
	private Extensions extensions;
	
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
	
	@NotNull
	@Column(name = "wait", nullable = false)
	private boolean wait;
	
	@NotNull
	@Column(name = "transfer", nullable = false)
	private boolean transfer;

	public ExtensionsWizzard() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Extensions getExtensions() {
		return extensions;
	}

	public void setExtensions(Extensions extensions) {
		this.extensions = extensions;
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

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public boolean isTransfer() {
		return transfer;
	}

	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
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
				+ longitud + ", record=" + record + ", limit=" + limit + ", wait=" + wait + ", transfer=" + transfer
				+ "]";
	}
	
}
