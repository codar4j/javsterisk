package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.LogAsterisk;
import com.web.javsterisk.entity.Parameter;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class LogAsteriskController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(LogAsteriskController.class);

	private ParameterDAO parameterDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<LogAsterisk> logURLs;		
		
	@PostConstruct
	public void initNewLogAsterisk() {
		log.info("initNewLogAsterisk");
		parameterDAO = new ParameterDAO();
		if(securityController.isAuthenticated()){
			logURLs = new ArrayList<LogAsterisk>(0);
			
			Parameter url = parameterDAO.findByName("asterisk.log.url");
			
			Parameter log1 = parameterDAO.findByName("asterisk.logQueue1");
			Parameter log2 = parameterDAO.findByName("asterisk.logQueue2");
			Parameter log3 = parameterDAO.findByName("asterisk.logQueue3");
			Parameter messages = parameterDAO.findByName("asterisk.logMessages");
			
			LogAsterisk logAsterisk = null;
			logAsterisk = new LogAsterisk(log1.getValue(), url.getValue() + log1.getValue());
			logURLs.add(logAsterisk);
			logAsterisk = new LogAsterisk(log2.getValue(), url.getValue() + log2.getValue());
			logURLs.add(logAsterisk);
			logAsterisk = new LogAsterisk(log3.getValue(), url.getValue() + log3.getValue());
			logURLs.add(logAsterisk);
			logAsterisk = new LogAsterisk(messages.getValue(), url.getValue() + messages.getValue());
			logURLs.add(logAsterisk);				
		}
	}

	public List<LogAsterisk> getLogURLs() {
		return logURLs;
	}

	public void setLogURLs(List<LogAsterisk> logURLs) {
		this.logURLs = logURLs;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}