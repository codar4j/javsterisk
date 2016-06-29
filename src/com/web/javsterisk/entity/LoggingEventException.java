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
@Table(name="logging_event_exception")
public class LoggingEventException implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	private Integer i;
	
	@NotNull
	private String trace_line;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	@NotNull
	private LoggingEvent loggingEvent;
		
	public String getTrace_line() {
		return trace_line;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getI() {
		return i;
	}
	public void setI(Integer i) {
		this.i = i;
	}
	public void setTrace_line(String trace_line) {
		this.trace_line = trace_line;
	}
	public LoggingEvent getLoggingEvent() {
		return loggingEvent;
	}
	public void setLoggingEvent(LoggingEvent loggingEvent) {
		this.loggingEvent = loggingEvent;
	}
	@Override
	public String toString() {
		return "LoggingEventException [id=" + id + ", i=" + i + ", trace_line="
				+ trace_line + "]";
	}
	
}