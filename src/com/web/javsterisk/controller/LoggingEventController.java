package com.web.javsterisk.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
    private String fileData;
    
    private String logFile = "C:/Temp/javsterisk/log/javsterisk.log";
	
    @PostConstruct
    public void readFile() {
    	log.info("Reading log : {}", logFile);
        File file = new File(logFile);

        try (FileInputStream fis = new FileInputStream(file)) {

            int content;
            while ((content = fis.read()) != -1) {
                fileData = fileData + (char)content;
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getdata() {
        return fileData;
    }
}