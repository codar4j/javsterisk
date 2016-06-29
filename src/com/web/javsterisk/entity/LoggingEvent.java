package com.web.javsterisk.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.web.javsterisk.util.Calendar;

/**
 * 
 * @author atorres
 *
 */
@Entity
@Table(name="logging_event")
public class LoggingEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long event_id;
	
	@NotNull
	private Long timestmp;
	
	@NotNull
	@Column(columnDefinition="LONGTEXT")
	private String formatted_message;
	
	@NotNull
	private String logger_name;
	
	@NotNull
	private String level_string;
	
	@NotNull
	private String thread_name;
	
	@NotNull
	private Integer reference_flag;
	
	@NotNull
	private String caller_filename;
	
	@NotNull
	private String caller_class;
	
	@NotNull
	private String caller_method;
	
	@NotNull
	private Integer caller_line;
	
	private String arg0;
	
	private String arg1;
	
	private String arg2;
	
	private String arg3;
	
	@Transient
	private String date;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loggingEvent")
	private List<LoggingEventProperty> loggingEventProperties;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loggingEvent")
	private List<LoggingEventException> loggingEventExceptions;
	
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}	
	public Long getTimestmp() {
		return timestmp;
	}
	public void setTimestmp(Long timestmp) {
		this.timestmp = timestmp;
	}
	public String getFormatted_message() {
		return formatted_message;
	}
	public void setFormatted_message(String formatted_message) {
		this.formatted_message = formatted_message;
	}
	public String getLogger_name() {
		return logger_name;
	}
	public void setLogger_name(String logger_name) {
		this.logger_name = logger_name;
	}
	public String getLevel_string() {
		return level_string;
	}
	public void setLevel_string(String level_string) {
		this.level_string = level_string;
	}	
	public String getThread_name() {
		return thread_name;
	}
	public void setThread_name(String thread_name) {
		this.thread_name = thread_name;
	}
	public Integer getReference_flag() {
		return reference_flag;
	}
	public void setReference_flag(Integer reference_flag) {
		this.reference_flag = reference_flag;
	}
	public String getCaller_filename() {
		return caller_filename;
	}
	public void setCaller_filename(String caller_filename) {
		this.caller_filename = caller_filename;
	}
	public String getCaller_class() {
		return caller_class;
	}
	public void setCaller_class(String caller_class) {
		this.caller_class = caller_class;
	}
	public String getCaller_method() {
		return caller_method;
	}
	public void setCaller_method(String caller_method) {
		this.caller_method = caller_method;
	}
	public Integer getCaller_line() {
		return caller_line;
	}
	public void setCaller_line(Integer caller_line) {
		this.caller_line = caller_line;
	}	
	public String getArg0() {
		return arg0;
	}
	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}	
	public String getArg1() {
		return arg1;
	}
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}	
	public String getArg2() {
		return arg2;
	}
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}	
	public String getArg3() {
		return arg3;
	}
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}	
	public String getDate() {		
		if(getTimestmp() == null){
			return date;
		} else {			
			Date _date = new Date(getTimestmp());
			date = Calendar.parseString(_date, "dd-MM-yyyy HH:mm:ss.SSS");				
			return date;	
		}		
	}
	public void setDate(String date) {
		this.date = date;
	}	
	public List<LoggingEventProperty> getLoggingEventProperties() {
		return loggingEventProperties;
	}
	public void setLoggingEventProperties(
			List<LoggingEventProperty> loggingEventProperties) {
		this.loggingEventProperties = loggingEventProperties;
	}	
	public List<LoggingEventException> getLoggingEventExceptions() {
		return loggingEventExceptions;
	}
	public void setLoggingEventExceptions(
			List<LoggingEventException> loggingEventExceptions) {
		this.loggingEventExceptions = loggingEventExceptions;
	}
	
	@Override
	public int hashCode() {
		return (event_id != null)
		        ? (this.getClass().hashCode() + event_id.hashCode())
		        : super.hashCode();
	}
	@Override
	public boolean equals(Object other) {
	    return (other instanceof LoggingEvent) && (event_id != null)
	        ? event_id.equals(((LoggingEvent) other).event_id)
	        : (other == this);
	}
	@Override
	public String toString() {
		return "LoggingEvent [event_id=" + event_id + ", timestmp=" + timestmp
				+ ", formatted_message=" + formatted_message + ", logger_name="
				+ logger_name + ", level_string=" + level_string
				+ ", thread_name=" + thread_name + ", reference_flag="
				+ reference_flag + ", caller_filename=" + caller_filename
				+ ", caller_class=" + caller_class + ", caller_method="
				+ caller_method + ", caller_line=" + caller_line + ", arg0="
				+ arg0 + ", arg1=" + arg1 + ", arg2=" + arg2 + ", arg3=" + arg3
				+ ", date=" + date + "]";
	}
	 
}