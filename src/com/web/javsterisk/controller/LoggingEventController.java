package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.LoggingEventDAO;
import com.web.javsterisk.entity.LoggingEvent;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class LoggingEventController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(LoggingEventController.class);
	
	private LoggingEventDAO loggingEventDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;	
	
	private Date since;
	
	private Date to;
	
	private List<LoggingEvent> loggingEvents;
	
	private List<LoggingEvent> loggingEventsFiltered;
	
	private SelectItem[] loggerLevelFilter;
	
	@PostConstruct
	public void initLoggingEvents() {
		
		loggerLevelFilter = new SelectItem[] {
			   	new SelectItem("", "Seleccione"), 
			   	new SelectItem(Level.ERROR, Level.ERROR.toString()), 
			   	new SelectItem(Level.WARN, Level.WARN.toString()), 
			   	new SelectItem(Level.INFO, Level.INFO.toString()), 
			   	new SelectItem(Level.DEBUG, Level.DEBUG.toString()), 
			   	new SelectItem(Level.TRACE, Level.TRACE.toString())};
		
		loggingEventDAO = new LoggingEventDAO();
		if(securityController.isAuthenticated()){
		if(since == null & to == null){
			Calendar _since = Calendar.getInstance();
			_since.set(Calendar.HOUR_OF_DAY, 0);
			_since.set(Calendar.MINUTE, 0);
			_since.set(Calendar.SECOND, 0);
			_since.set(Calendar.MILLISECOND, 0);
			
			Calendar _to = Calendar.getInstance();
			_to.set(Calendar.HOUR_OF_DAY, 23);
			_to.set(Calendar.MINUTE, 59);
			_to.set(Calendar.SECOND, 59);
			_to.set(Calendar.MILLISECOND, 999);			
			
			since = new Date(_since.getTimeInMillis());
			to = new Date(_to.getTimeInMillis());
		}
			log.debug("since : {}", since);
			log.debug("to : {}", to);	
			loggingEvents = loggingEventDAO.findAllRecordsFilteredByDate(since, to, false);
		}
	}
	
	public void searchByDate() {
		Calendar _since = Calendar.getInstance();
		_since.setTimeInMillis(since.getTime());
		_since.set(Calendar.HOUR_OF_DAY, 0);
		_since.set(Calendar.MINUTE, 0);
		_since.set(Calendar.SECOND, 0);
		_since.set(Calendar.MILLISECOND, 0);
		
		Calendar _to = Calendar.getInstance();
		_to.setTimeInMillis(to.getTime());
		_to.set(Calendar.HOUR_OF_DAY, 23);
		_to.set(Calendar.MINUTE, 59);
		_to.set(Calendar.SECOND, 59);
		_to.set(Calendar.MILLISECOND, 999);
		
		since = new Date(_since.getTimeInMillis());
		to = new Date(_to.getTimeInMillis());
		
		log.info("sarting searchByDate()...");
		log.debug("since : {}", since);
		log.debug("to : {}", to);		
		loggingEvents = loggingEventDAO.findAllRecordsFilteredByDate(since, to, false);
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public List<LoggingEvent> getLoggingEvents() {
		return loggingEvents;
	}

	public void setLoggingEvents(List<LoggingEvent> loggingEvents) {
		this.loggingEvents = loggingEvents;
	}

	public List<LoggingEvent> getLoggingEventsFiltered() {
		return loggingEventsFiltered;
	}

	public void setLoggingEventsFiltered(List<LoggingEvent> loggingEventsFiltered) {
		this.loggingEventsFiltered = loggingEventsFiltered;
	}

	public SelectItem[] getLoggerLevelFilter() {
		return loggerLevelFilter;
	}

	public void setLoggerLevelFilter(SelectItem[] loggerLevelFilter) {
		this.loggerLevelFilter = loggerLevelFilter;
	}
	
	
//    private String fileData;
//    
//    private String logFile = "C:/Temp/javsterisk/log/javsterisk.log";
//	
//    @PostConstruct
//    public void readFile() {
//    	log.info("Reading log : {}", logFile);
//        File file = new File(logFile);
//
//        try (FileInputStream fis = new FileInputStream(file)) {
//
//            int content;
//            while ((content = fis.read()) != -1) {
//                fileData = fileData + (char)content;
//            }
//
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public String getdata() {
//        return fileData;
//    }
}