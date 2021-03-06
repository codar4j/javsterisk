package com.web.javsterisk.entity;

// Generated 07-03-2014 02:28:25 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Freddy Moran
 *
 */
@Embeddable
public class ExtensionsId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(max = 20)
	@Column(name = "context", nullable = false, length = 20)
	private String context = "from-sip";

	@NotNull
	@Size(max = 20)
	@Column(name = "exten", nullable = false, length = 20)
	private String exten = "";

	@NotNull	
	@Column(name = "priority", nullable = false)
	private byte priority = 0;

	public ExtensionsId() {
	}

	public ExtensionsId(String context, String exten, byte priority) {
		this.context = context;
		this.exten = exten;
		this.priority = priority;
	}
	
	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public String getExten() {
		return this.exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}
	
	public byte getPriority() {
		return this.priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ExtensionsId))
			return false;
		ExtensionsId castOther = (ExtensionsId) other;

		return ((this.getContext() == castOther.getContext()) || (this
				.getContext() != null && castOther.getContext() != null && this
				.getContext().equals(castOther.getContext())))
				&& ((this.getExten() == castOther.getExten()) || (this
						.getExten() != null && castOther.getExten() != null && this
						.getExten().equals(castOther.getExten())))
				&& (this.getPriority() == castOther.getPriority());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getContext() == null ? 0 : this.getContext().hashCode());
		result = 37 * result
				+ (getExten() == null ? 0 : this.getExten().hashCode());
		result = 37 * result + this.getPriority();
		return result;
	}

}
