package com.web.javsterisk.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Parameter;

@ManagedBean
@ViewScoped
public class SystemController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(SystemController.class);
	
    private ParameterDAO parameterDAO;
    
    private String osOption;
    
    private Date dateHour;
    
    private String asteriskOption;
    
    @PostConstruct
    public void initSystem() {
    	parameterDAO = new ParameterDAO();
    }

	public void shutdownOS() {
		log.debug("Starting shutdownOS()");
		try {
			Parameter parameter = parameterDAO.findByName("shutdown.os");
			String cmd = parameter.getValue();
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Apagando!", "El sistema se esta apagando"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el apagado"));
		}
	}
	
	public void restartOS() {
		log.debug("Starting restartOS()");
		try {			
			Parameter parameter = parameterDAO.findByName("restart.os");
			String cmd = parameter.getValue();
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reiniciando!", "El sistema se esta reiniciando"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el reinicio"));
		}
	}
	
	public void changeDate() {
		log.debug("Starting changeDate()");
		try {			
			Parameter parameter = parameterDAO.findByName("change.date.os");
			String cmd = parameter.getValue();
			cmd += " '" + dateHour +  "'";
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio Fecha!", "El sistema esta cambiando Fecha y Hora"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el cambio de Fecha y Hora"));
		}		
	}
	
	
	public void startAsterisk() {
		log.debug("Starting startAsterisk()");
		try {			
			Parameter parameter = parameterDAO.findByName("start.service.asterisk");
			String cmd = parameter.getValue();
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Iciando!", "El servicio asterisk se esta iniciando"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el Inicio"));
		}
	}
	
	public void stopAsterisk() {
		log.debug("Stop startAsterisk()");
		try {			
			Parameter parameter = parameterDAO.findByName("stop.service.asterisk");
			String cmd = parameter.getValue();
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Apagando!", "El servicio asterisk se esta apagando"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el Apagado"));
		}
	}
	
	public void restartAsterisk() {
		log.debug("Restarting startAsterisk()");
		try {			
			Parameter parameter = parameterDAO.findByName("restart.service.asterisk");
			String cmd = parameter.getValue();
			log.trace("cmd : {}", cmd);
			Runtime.getRuntime().exec(cmd);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reiciando!", "El servicio asterisk se esta reiniciando"));
		} catch (IOException e) {
			log.error("IOException", e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "IOException!", "Error en el Reinicio"));
		}
	}
	
	public void actionOS(){
		if(osOption.equals("1")){
			shutdownOS();
		} else if(osOption.equals("2")) {
			restartOS();
		}						
		if(dateHour != null){
				changeDate();
		}
	}
	
	public void actionAsterisk(){
		if(asteriskOption.equals("1")){
			startAsterisk();
		} else if(asteriskOption.equals("2")){
			stopAsterisk();
		} else if(asteriskOption.equals("3")){
			restartAsterisk();
		}
	}

	public String getOsOption() {
		return osOption;
	}

	public void setOsOption(String osOption) {
		this.osOption = osOption;
	}

	public Date getDateHour() {
		return dateHour;
	}

	public void setDateHour(Date dateHour) {
		this.dateHour = dateHour;
	}

	public String getAsteriskOption() {
		return asteriskOption;
	}

	public void setAsteriskOption(String asteriskOption) {
		this.asteriskOption = asteriskOption;
	}
	
}
