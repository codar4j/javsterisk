package com.web.javsterisk.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author atorres
 *
 */
@Entity
@Table(name="logging_event_property")
public class LoggingEventProperty implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@NotNull
	private String mapped_key;	
	
	@NotNull
	private String mapped_value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	@NotNull
	private LoggingEvent loggingEvent;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMapped_key() {
		return mapped_key;
	}
	public void setMapped_key(String mapped_key) {
		this.mapped_key = mapped_key;
	}
	public String getMapped_value() {
		return mapped_value;
	}
	public void setMapped_value(String mapped_value) {
		this.mapped_value = mapped_value;
	}	
	public LoggingEvent getLoggingEvent() {
		return loggingEvent;
	}
	public void setLoggingEvent(LoggingEvent loggingEvent) {
		this.loggingEvent = loggingEvent;
	}
	@Override
	public String toString() {
		return "LoggingEventProperty [id=" + id + ", mapped_key=" + mapped_key
				+ ", mapped_value=" + mapped_value + "]";
	}
	 
}