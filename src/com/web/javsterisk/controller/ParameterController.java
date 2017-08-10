package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.web.asterisk4j.enumeration.RoleType;
import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Parameter;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class ParameterController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ParameterController.class);

	private ParameterDAO parameterDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Parameter> parametersFiltered;

	private Parameter newParameter;
				
	private Parameter[] selectedParameters;
	
	private int selectedParametersSize;
	
	private List<Parameter> parameters;   
	
	@PostConstruct
	public void initNewParameter() {
		parameterDAO = new ParameterDAO();
		if(securityController.isAuthenticated()){
			parameters = parameterDAO.findAllOrderedByName();
			newParameter = new Parameter();			
		}
	}
	
	public void register() throws Exception { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {		
			parameterDAO.register(newParameter);
			log.info("Registration successful");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));			
			initNewParameter();
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}		
	}

	public void modifier() throws Exception { 
		log.info("Start modifier()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {
			Parameter selectedParameter = selectedParameters[0];												
			parameterDAO.modifier(selectedParameter);
			log.info("Modification successful");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
			initNewParameter();
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public void deleter() throws Exception { 
		log.info("Start deleter()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {		
			for(Parameter selectedParameter : selectedParameters){				
				parameterDAO.deleter(selectedParameter);
				log.info("Eliminacion exitosa del usuario : {}", selectedParameter.getName());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del usuario : " + selectedParameter.getName()));
			}
			initNewParameter();      
		} else {			
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public List<Parameter> getParametersFiltered() {
		return parametersFiltered;
	}

	public void setParametersFiltered(List<Parameter> parametersFiltered) {
		this.parametersFiltered = parametersFiltered;
	}

	public Parameter getNewParameter() {
		return newParameter;
	}

	public void setNewParameter(Parameter newParameter) {
		this.newParameter = newParameter;
	}

	public Parameter[] getSelectedParameters() {
		return selectedParameters;
	}

	public void setSelectedParameters(Parameter[] selectedParameters) {
		this.selectedParameters = selectedParameters;
	}

	public int getSelectedParametersSize() {
		selectedParametersSize = this.selectedParameters == null ? 0 : this.selectedParameters.length;
		return selectedParametersSize;
	}

	public void setSelectedParametersSize(int selectedParametersSize) {
		this.selectedParametersSize = selectedParametersSize;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
		
}