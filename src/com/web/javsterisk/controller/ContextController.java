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
import org.hibernate.exception.ConstraintViolationException;

import com.web.javsterisk.dao.ContextDAO;
import com.web.javsterisk.entity.Context;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

/**
 * 
 * @author Freddy Moran
 *
 */
@ManagedBean
@ViewScoped
public class ContextController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ContextController.class);

	private ContextDAO contextDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Context> contextsFiltered;

	private Context newContext;
				
	private Context[] selectedContexts;
	
	private int selectedContextsSize;
	
	private List<Context> contexts;
	
	@PostConstruct
	public void initNewContext() {
		contextDAO = new ContextDAO();
		if(securityController.isAuthenticated()){
			newContext = new Context();	
			contexts = contextDAO.findAllOrderedById();
		}
	}
	
	public void register() { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {		
			try {
				contextDAO.register(newContext);
				log.info("Registration successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));			
				initNewContext();
			} catch (Exception e) {				
				if(e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					log.error("ConstraintViolationException", e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El contexto ya existe"));
				} else {
					log.error("Exception", e);
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}		
	}

	public void modifier() throws Exception { 
		log.info("Start modifier()");	    
		if ( securityController.isAdministrator() ) {
			Context selectedContext = selectedContexts[0];												
			contextDAO.modifier(selectedContext);
			log.info("Modification successful");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
			initNewContext();
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public void deleter() throws Exception { 
		log.info("Start deleter()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {		
			for(Context selectedContext : selectedContexts){				
				contextDAO.deleter(selectedContext);
				log.info("Eliminacion exitosa del contexto : {}", selectedContext.getName());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del contexto : " + selectedContext.getName()));
			}
			initNewContext();      
		} else {			
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}
	
	public List<Context> getContextsFiltered() {
		return contextsFiltered;
	}

	public void setContextsFiltered(List<Context> contextsFiltered) {
		this.contextsFiltered = contextsFiltered;
	}

	public Context getNewContext() {
		return newContext;
	}

	public void setNewContext(Context newContext) {
		this.newContext = newContext;
	}

	public Context[] getSelectedContexts() {
		return selectedContexts;
	}

	public void setSelectedContexts(Context[] selectedContexts) {
		this.selectedContexts = selectedContexts;
	}

	public int getSelectedContextsSize() {
		selectedContextsSize = this.selectedContexts == null ? 0 : this.selectedContexts.length;
		return selectedContextsSize;
	}

	public void setSelectedContextsSize(int selectedContextsSize) {
		this.selectedContextsSize = selectedContextsSize;
	}

	public List<Context> getContexts() {
		return contexts;
	}

	public void setContexts(List<Context> contexts) {
		this.contexts = contexts;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
		
}