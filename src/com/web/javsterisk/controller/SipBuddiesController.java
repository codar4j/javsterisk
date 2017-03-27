package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

//import com.web.asterisk4j.enumeration.RoleType;
import com.web.javsterisk.dao.SipBuddiesDAO;
import com.web.javsterisk.entity.SipBuddies;
import com.web.javsterisk.enumeration.SipType;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class SipBuddiesController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(SipBuddiesController.class);

	private SipBuddiesDAO sipBuddiesDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<SipBuddies> sipBuddiesesFiltered;

	private SipBuddies newSipBuddies;
				
	private SipBuddies[] selectedSipBuddieses;
	
	private int selectedSipBuddiesesSize;
	
	private String secretEdit;
	
	private List<SipBuddies> sipBuddieses;   
	   
	private SelectItem[] sipBuddiesesFilter; 
	
	private List<SipType> sipTypes;
	
	private SelectItem[] sipTypesFilter;  
	
	@PostConstruct
	public void initNewUser() {	
		sipBuddiesDAO = new SipBuddiesDAO();
		if(securityController.isAuthenticated()){
			
			sipTypes = new ArrayList<SipType>(0);
			   
			   sipTypes.add(SipType.USER);
			   sipTypes.add(SipType.PEERS);
			   sipTypes.add(SipType.FRIEND);
			   
			   sipTypesFilter = new SelectItem[] {
					   	new SelectItem("", "Seleccione"), 
					   	new SelectItem(SipType.USER, SipType.USER.getValue()),
					   	new SelectItem(SipType.PEERS, SipType.PEERS.getValue()),
					   	new SelectItem(SipType.FRIEND, SipType.FRIEND.getValue())};
			
			sipBuddieses = sipBuddiesDAO.findAllOrderedByField("name", true);
			
			if(sipBuddieses.size() > 0){
				   sipBuddiesesFilter = new SelectItem[sipBuddieses.size() + 1];
				   
				   sipBuddiesesFilter[0] = new SelectItem("", "Seleccione");
				   
				   int i = 1;
				   
				   for(SipBuddies sipBuddies : sipBuddieses){
					   sipBuddiesesFilter[i] = new SelectItem(sipBuddies.getName(), sipBuddies.getName());
					   
					   i++;
				   }
			   } else {
				   sipBuddiesesFilter = new SelectItem[1];
				   sipBuddiesesFilter[0] = new SelectItem("", "Seleccione");
			   }
			
			newSipBuddies = new SipBuddies();
		}
	}
	
	public void register() { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {
			try {
				newSipBuddies.setDefaultuser(newSipBuddies.getName());
				newSipBuddies.setType(newSipBuddies.getType().toLowerCase());
				sipBuddiesDAO.register(newSipBuddies);
				log.info("Registration successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));			
				initNewUser();
			} catch (ConstraintViolationException e) {
				log.error("ConstraintViolationException", e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La extension ya existe"));
			} catch (Exception e) {						
				log.error("Exception", e);				
			}
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}		
	}

	public void modifier() { 
		log.info("Start modifier()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {
			try {
				SipBuddies selectedSipBuddies = selectedSipBuddieses[0];	
				if(secretEdit != null && !secretEdit.trim().equalsIgnoreCase("")){				
					selectedSipBuddies.setSecret(secretEdit);	
				}
				sipBuddiesDAO.modifier(selectedSipBuddies);
				log.info("Modification successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
				initNewUser();
			} catch (Exception e) {				
				if(e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					log.error("ConstraintViolationException", e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La extension ya existe"));
				} else {
					log.error("Exception", e);
				}
			}
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public void deleter() throws Exception { 
		log.info("Start deleter()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {		
			for(SipBuddies selectedSipBuddies : selectedSipBuddieses){				
				sipBuddiesDAO.deleter(selectedSipBuddies);
				log.info("Eliminacion exitosa del usuario : {}", selectedSipBuddies.getId());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del usuario : " + selectedSipBuddies.getId()));
			}
			initNewUser();      
		} else {			
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}
	
	public List<SipBuddies> getSipBuddiesesFiltered() {
		return sipBuddiesesFiltered;
	}

	public void setSipBuddiesesFiltered(List<SipBuddies> sipBuddiesesFiltered) {
		this.sipBuddiesesFiltered = sipBuddiesesFiltered;
	}

	public SipBuddies getNewSipBuddies() {
		return newSipBuddies;
	}

	public void setNewSipBuddies(SipBuddies newSipBuddies) {
		this.newSipBuddies = newSipBuddies;
	}

	public SipBuddies[] getSelectedSipBuddieses() {
		return selectedSipBuddieses;
	}

	public void setSelectedSipBuddieses(SipBuddies[] selectedSipBuddieses) {
		this.selectedSipBuddieses = selectedSipBuddieses;
	}

	public int getSelectedSipBuddiesesSize() {
		selectedSipBuddiesesSize = this.selectedSipBuddieses == null ? 0 : this.selectedSipBuddieses.length;
		return selectedSipBuddiesesSize;
	}

	public void setSelectedSipBuddiesesSize(int selectedSipBuddiesesSize) {
		this.selectedSipBuddiesesSize = selectedSipBuddiesesSize;
	}

	public String getSecretEdit() {
		return secretEdit;
	}

	public void setSecretEdit(String secretEdit) {
		this.secretEdit = secretEdit;
	}

	public List<SipBuddies> getSipBuddieses() {
		return sipBuddieses;
	}

	public void setSipBuddieses(List<SipBuddies> sipBuddieses) {
		this.sipBuddieses = sipBuddieses;
	}

	public SelectItem[] getSipBuddiesesFilter() {
		return sipBuddiesesFilter;
	}

	public void setSipBuddiesesFilter(SelectItem[] sipBuddiesesFilter) {
		this.sipBuddiesesFilter = sipBuddiesesFilter;
	}
	
	public List<SipType> getSipTypes() {
		return sipTypes;
	}

	public void setSipTypes(List<SipType> sipTypes) {
		this.sipTypes = sipTypes;
	}

	public SelectItem[] getSipTypesFilter() {
		return sipTypesFilter;
	}

	public void setSipTypesFilter(SelectItem[] sipTypesFilter) {
		this.sipTypesFilter = sipTypesFilter;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}