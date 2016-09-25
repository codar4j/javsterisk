package com.web.javsterisk.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
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
//import org.primefaces.event.ToggleEvent;

import com.web.javsterisk.dao.LogDAO;
import com.web.javsterisk.entity.Log;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class LogController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(LogController.class);
	
	private LogDAO logDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private final String TOGGLE_VISIBLE = "VISIBLE";	
	
	private Date since;
	
	private Date to;		
	
	private List<Log> logs;
	
	private List<Log> logFiltered;
	
	private SelectItem[] logLevelFilter;
	
	@PostConstruct
	public void initLoggingEvents() {
		
		logLevelFilter = new SelectItem[] {
			   	new SelectItem("", "Seleccione"), 
			   	new SelectItem(Level.ERROR, Level.ERROR.toString()), 
			   	new SelectItem(Level.WARN, Level.WARN.toString()), 
			   	new SelectItem(Level.INFO, Level.INFO.toString()), 
			   	new SelectItem(Level.DEBUG, Level.DEBUG.toString()), 
			   	new SelectItem(Level.TRACE, Level.TRACE.toString())};
		
		logDAO = new LogDAO();
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
//		loggingEvents = loggingEventDAO.findAllRecordsOrderedByTimestmp();
//		logs = logDAO.findAllRecordsFilteredByDate(since.getTime(), to.getTime());		
		}
		
		try {
			Socket s;
			while (true) {
				try {
					s = new Socket("localhost", 9500);
					break;
				} catch (java.net.ConnectException e) { // Assume that the host
														// isn't available yet,
														// wait
					Thread.currentThread();
					// a moment, then try again.
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String line;

			while ((line = in.readLine()) != null){
				System.err.println(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
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
		logs = logDAO.findAllRecordsFilteredByDate(since.getTime(), to.getTime());
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
	
	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public List<Log> getLogFiltered() {
		return logFiltered;
	}

	public void setLogFiltered(List<Log> logFiltered) {
		this.logFiltered = logFiltered;
	}

	public SelectItem[] getLogLevelFilter() {
		return logLevelFilter;
	}

	public void setLogLevelFilter(SelectItem[] logLevelFilter) {
		this.logLevelFilter = logLevelFilter;
	}

//	public void onRowToggle(ToggleEvent event) {
//		log.debug("onRowToggle id : {} - Status : {}", ((Log) event.getData()).getId(), event.getVisibility());
//		if(event.getVisibility().toString().equals(TOGGLE_VISIBLE)){			
//			LoggingEvent le = loggingEventDAO.findById(((LoggingEvent) event.get).getEvent_id());
//			setLoggingEventProperties(le.getLoggingEventProperties());
//			setLoggingEventExceptions(le.getLoggingEventExceptions());
//		}
//    }

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}