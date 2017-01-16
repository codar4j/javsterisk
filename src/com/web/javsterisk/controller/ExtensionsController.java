package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

//import com.web.asterisk4j.enumeration.RoleType;
import com.web.javsterisk.dao.ExtensionsDAO;
import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Extensions;
import com.web.javsterisk.entity.ExtensionsId;
import com.web.javsterisk.entity.ExtensionsWizzard;
import com.web.javsterisk.entity.Parameter;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

/**
 * 
 * @author Freddy Moran
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class ExtensionsController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ExtensionsController.class);
	
	private final String EXT_PREFIX = "_";
	
	private final String EXT_CHAR = "X";
	
	private final String APP_ANSWER = "Answer";
	
	private final String APP_SET = "Set";
	
	private final String APP_MIX_MONITOR = "MixMonitor";
	
	private final String APP_DIAL = "Dial";
	
	private final String APP_HANGUP = "Hangup";
	
	private ExtensionsDAO extensionsDAO;
	
	private ParameterDAO parameterDAO;
	
	Parameter param_record_path;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Extensions> extensionesFiltered;

	private Extensions newExtensions;
				
	private Extensions[] selectedExtensiones;
	
	private int selectedExtensionesSize;
	
	private List<Extensions> extensiones;
	
	@PostConstruct
	public void initNewExtensions() {
		parameterDAO = new ParameterDAO();		
		extensionsDAO = new ExtensionsDAO();
		log.info("@PostConstruct Extensions");
		if(securityController.isAuthenticated()){
			param_record_path = parameterDAO.findByName("asterisk.recorder.path");
			
			ExtensionsId eid = new ExtensionsId();
			newExtensions = new Extensions();	
			newExtensions.setId(eid);
			
			ExtensionsWizzard wizzard = new ExtensionsWizzard();
			newExtensions.setExtensionsWizzard(wizzard);
			
			extensiones = extensionsDAO.findAllOrderedById();
		}
	}
	
	public void register() { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {		
			try {
				
				newExtensions.setId_1(extensionsDAO.findAllOrderedById().size() + 1);
				
				Extensions[] extensions = makeExtensions();
			
				for(int i = 0 ; i < extensions.length; i ++) {
					extensionsDAO.register(extensions[i]);
				}
				
				log.info("Registration successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));			
				initNewExtensions();
			} catch (Exception e) {				
				if(e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					log.error("ConstraintViolationException", e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El Dial Plan ya existe"));
				} else {
					log.error("Exception", e);
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}		
	}

	private Extensions[] makeExtensions() {
		
		Extensions[] extensions = null;
		
		String extension = StringUtils.rightPad(EXT_PREFIX + newExtensions.getExtensionsWizzard().getDigito(), newExtensions.getExtensionsWizzard().getLongitud() + 1, EXT_CHAR);
		
		if(!newExtensions.getExtensionsWizzard().isRecord() && !newExtensions.getExtensionsWizzard().isLimit() &&
				!newExtensions.getExtensionsWizzard().isTransfer() && !newExtensions.getExtensionsWizzard().isWait()) {
			
			newExtensions.getId().setExten(extension);
			
			extensions = new Extensions[3];
			
			extensions[0] = new Extensions();
			extensions[0].setId(new ExtensionsId());
			extensions[0].getId().setContext(newExtensions.getId().getContext());
			extensions[0].getId().setExten(newExtensions.getId().getExten());
			extensions[0].getId().setPriority((byte)1);
			extensions[0].setApp(APP_ANSWER);
			extensions[0].setId_1(newExtensions.getId_1());
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)2);
			extensions[1].setApp(APP_DIAL);
			extensions[1].setAppdata("SIP/${EXTEN}");
			extensions[1].setId_1(newExtensions.getId_1() + 1);
			
			extensions[2] = new Extensions();
			extensions[2].setId(new ExtensionsId());
			extensions[2].getId().setContext(newExtensions.getId().getContext());
			extensions[2].getId().setExten(newExtensions.getId().getExten());
			extensions[2].getId().setPriority((byte)3);
			extensions[2].setApp(APP_HANGUP);
			extensions[2].setId_1(newExtensions.getId_1() + 2);
			
		} else if (newExtensions.getExtensionsWizzard().isRecord() && !newExtensions.getExtensionsWizzard().isLimit() &&
				!newExtensions.getExtensionsWizzard().isTransfer() && !newExtensions.getExtensionsWizzard().isWait()) {
			
			extensions = new Extensions[5];
			
			extensions[0] = new Extensions();
			extensions[0].setId(new ExtensionsId());
			extensions[0].getId().setContext(newExtensions.getId().getContext());
			extensions[0].getId().setExten(newExtensions.getId().getExten());
			extensions[0].getId().setPriority((byte)1);
			extensions[0].setApp(APP_ANSWER);
			extensions[0].setId_1(newExtensions.getId_1());
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)2);
			extensions[1].setApp(APP_SET);
			extensions[1].setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%-%H%M%S)}-${CALLERID(num)}");
			extensions[1].setId_1(newExtensions.getId_1() + 1);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)3);
			extensions[1].setApp(APP_MIX_MONITOR);
			extensions[1].setAppdata(param_record_path.getValue() +" ​${MONITOR_FILENAME}.wav,b");
			extensions[1].setId_1(newExtensions.getId_1() + 2);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)4);
			extensions[1].setApp(APP_DIAL);
			extensions[1].setAppdata("SIP/${EXTEN}");
			extensions[1].setId_1(newExtensions.getId_1() + 3);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)5);
			extensions[1].setApp(APP_HANGUP);
			extensions[1].setId_1(newExtensions.getId_1() + 4);
			
		} else if (newExtensions.getExtensionsWizzard().isRecord() && newExtensions.getExtensionsWizzard().isLimit() &&
				!newExtensions.getExtensionsWizzard().isTransfer() && !newExtensions.getExtensionsWizzard().isWait()) {
			
			extensions = new Extensions[5];
			
			extensions[0] = new Extensions();
			extensions[0].setId(new ExtensionsId());
			extensions[0].getId().setContext(newExtensions.getId().getContext());
			extensions[0].getId().setExten(newExtensions.getId().getExten());
			extensions[0].getId().setPriority((byte)1);
			extensions[0].setApp(APP_ANSWER);
			extensions[0].setId_1(newExtensions.getId_1());
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)2);
			extensions[1].setApp(APP_SET);
			extensions[1].setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%d-%H%M%S)}-${CALLERID(num)}");
			extensions[1].setId_1(newExtensions.getId_1() + 1);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)3);
			extensions[1].setApp(APP_MIX_MONITOR);
			extensions[1].setAppdata(param_record_path.getValue() +" ${MONITOR_FILENAME}.wav,b");
			extensions[1].setId_1(newExtensions.getId_1() + 2);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)4);
			extensions[1].setApp(APP_DIAL);
			extensions[1].setAppdata("SIP/${EXTEN},,L(120000:30000:10000)");
			extensions[1].setId_1(newExtensions.getId_1() + 3);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)5);
			extensions[1].setApp(APP_HANGUP);
			extensions[1].setId_1(newExtensions.getId_1() + 4);
			
		} else if (newExtensions.getExtensionsWizzard().isRecord() && newExtensions.getExtensionsWizzard().isLimit() &&
				!newExtensions.getExtensionsWizzard().isTransfer() && newExtensions.getExtensionsWizzard().isWait()) {
			
			extensions = new Extensions[5];
			
			extensions[0] = new Extensions();
			extensions[0].setId(new ExtensionsId());
			extensions[0].getId().setContext(newExtensions.getId().getContext());
			extensions[0].getId().setExten(newExtensions.getId().getExten());
			extensions[0].getId().setPriority((byte)1);
			extensions[0].setApp(APP_ANSWER);
			extensions[0].setId_1(newExtensions.getId_1());
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)2);
			extensions[1].setApp(APP_SET);
			extensions[1].setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%d-%H%M%S)}-${CALLERID(num)}");
			extensions[1].setId_1(newExtensions.getId_1() + 1);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)3);
			extensions[1].setApp(APP_MIX_MONITOR);
			extensions[1].setAppdata(param_record_path.getValue() +" ${MONITOR_FILENAME}.wav,b");
			extensions[1].setId_1(newExtensions.getId_1() + 2);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)4);
			extensions[1].setApp(APP_DIAL);
			extensions[1].setAppdata("SIP/${EXTEN},15,L(120000:30000:10000)");
			extensions[1].setId_1(newExtensions.getId_1() + 3);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)5);
			extensions[1].setApp(APP_HANGUP);
			extensions[1].setId_1(newExtensions.getId_1() + 4);
			
		} else if (newExtensions.getExtensionsWizzard().isRecord() && newExtensions.getExtensionsWizzard().isLimit() &&
				newExtensions.getExtensionsWizzard().isTransfer() && newExtensions.getExtensionsWizzard().isWait()) {
			
			extensions = new Extensions[5];
			
			extensions[0] = new Extensions();
			extensions[0].setId(new ExtensionsId());
			extensions[0].getId().setContext(newExtensions.getId().getContext());
			extensions[0].getId().setExten(newExtensions.getId().getExten());
			extensions[0].getId().setPriority((byte)1);
			extensions[0].setApp(APP_ANSWER);
			extensions[0].setId_1(newExtensions.getId_1());
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)2);
			extensions[1].setApp(APP_SET);
			extensions[1].setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%d-%H%M%S)}-${CALLERID(num)}");
			extensions[1].setId_1(newExtensions.getId_1() + 1);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)3);
			extensions[1].setApp(APP_MIX_MONITOR);
			extensions[1].setAppdata(param_record_path.getValue() +" ${MONITOR_FILENAME}.wav,b");
			extensions[1].setId_1(newExtensions.getId_1() + 2);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)4);
			extensions[1].setApp(APP_DIAL);
			extensions[1].setAppdata("SIP/103&SIP/104,15,L(120000:30000:10000)");
			extensions[1].setId_1(newExtensions.getId_1() + 3);
			
			extensions[1] = new Extensions();
			extensions[1].setId(new ExtensionsId());
			extensions[1].getId().setContext(newExtensions.getId().getContext());
			extensions[1].getId().setExten(newExtensions.getId().getExten());
			extensions[1].getId().setPriority((byte)5);
			extensions[1].setApp(APP_HANGUP);
			extensions[1].setId_1(newExtensions.getId_1() + 4);
			
		}
		
		extension = null;
		
		return extensions;
	}

	public void modifier() throws Exception { 
		log.info("Start modifier()");	    
		if ( securityController.isAdministrator() ) {
			Extensions selectedExtensions = selectedExtensiones[0];												
			extensionsDAO.modifier(selectedExtensions);
			log.info("Modification successful");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
			initNewExtensions();
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public void deleter() throws Exception { 
		log.info("Start deleter()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {		
			for(Extensions selectedExtensions : selectedExtensiones){				
				extensionsDAO.deleter(selectedExtensions);
				log.info("Eliminacion exitosa del usuario : {}", selectedExtensions.getId_1());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del usuario : " + selectedExtensions.getId_1()));
			}
			initNewExtensions();      
		} else {			
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	
	public List<Extensions> getExtensionesFiltered() {
		return extensionesFiltered;
	}

	public void setExtensionesFiltered(List<Extensions> extensionesFiltered) {
		this.extensionesFiltered = extensionesFiltered;
	}

	public Extensions getNewExtensions() {
		return newExtensions;
	}

	public void setNewExtensions(Extensions newExtensions) {
		this.newExtensions = newExtensions;
	}

	public Extensions[] getSelectedExtensiones() {
		return selectedExtensiones;
	}

	public void setSelectedExtensiones(Extensions[] selectedExtensiones) {
		this.selectedExtensiones = selectedExtensiones;
	}

	public int getSelectedExtensionesSize() {
		selectedExtensionesSize = this.selectedExtensiones == null ? 0 : this.selectedExtensiones.length;
		return selectedExtensionesSize;
	}

	public void setSelectedExtensionesSize(int selectedExtensionesSize) {
		this.selectedExtensionesSize = selectedExtensionesSize;
	}

	public List<Extensions> getExtensiones() {
		return extensiones;
	}

	public void setExtensiones(List<Extensions> extensiones) {
		this.extensiones = extensiones;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
		
}