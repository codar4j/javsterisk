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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import com.web.javsterisk.dao.ExtensionsDAO;
import com.web.javsterisk.dao.ExtensionsWizzardDAO;
import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Extensions;
import com.web.javsterisk.entity.ExtensionsId;
import com.web.javsterisk.entity.ExtensionsWizzard;
import com.web.javsterisk.entity.Parameter;

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
	
	private ExtensionsWizzardDAO extensionsWizzardDAO;
	
	private ParameterDAO parameterDAO;
	
	private Parameter param_record_path;
	
	private String context;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Extensions> extensionesFiltered;

	private Extensions newExtensions;
	
	private ExtensionsWizzard newExtensionsWizzard;
				
	private Extensions[] selectedExtensiones;
	
	private int selectedExtensionesSize;
	
	private List<Extensions> extensiones;
	
	@PostConstruct
	public void initNewExtensions() {
		parameterDAO = new ParameterDAO();		
		extensionsDAO = new ExtensionsDAO();
		extensionsWizzardDAO = new ExtensionsWizzardDAO();
		log.info("@PostConstruct Extensions");
		if(securityController.isAuthenticated()){
			context = null;
			param_record_path = parameterDAO.findByName("asterisk.recorder.path");			
			newExtensionsWizzard = new ExtensionsWizzard();			
			extensiones = extensionsDAO.findAllOrderedById();
		}
	}
	
	public void register() { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {		
			try {
				
				makeExtensions();

				extensionsWizzardDAO.register(newExtensionsWizzard);
				
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

	private void makeExtensions() {
		
		newExtensionsWizzard.setExtensions(new ArrayList<Extensions>(0));
		
		int id = extensionsDAO.findAllOrderedById().size() + 1;
		
		String extension = StringUtils.rightPad(EXT_PREFIX + newExtensionsWizzard.getDigito(), newExtensionsWizzard.getLongitud() + 1, EXT_CHAR);
		
		log.info("-> context : {}", context);
		log.info("-> extesnion generated : {}", extension);		
		
		Extensions answer = new Extensions();
		answer.setApp(APP_ANSWER);		
		
		Extensions set = new Extensions();
		set.setApp(APP_SET);
		set.setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%-%H%M%S)}-${CALLERID(num)}");
		
		Extensions monitor = new Extensions();
		monitor.setApp(APP_MIX_MONITOR);
		monitor.setAppdata(param_record_path.getValue() +" ​${MONITOR_FILENAME}.wav,b");
		
		Extensions dial = new Extensions();
		dial.setApp(APP_DIAL);
		
		Extensions hangup = new Extensions();
		hangup.setApp(APP_HANGUP);
		
		String limit = "";
		
		if(newExtensionsWizzard.isLimit()) {
			
			limit = ",L(" + newExtensionsWizzard.getTimeLimit() + "::)";
			
			if(!"".equals(newExtensionsWizzard.getFirstAlert().trim()) 
					&& "".equals(newExtensionsWizzard.getSecondAlert().trim())) {
				limit = ",L(" + newExtensionsWizzard.getTimeLimit() + ":" 
					+ newExtensionsWizzard.getFirstAlert() + ":)";
			}
			
			if(!"".equals(newExtensionsWizzard.getFirstAlert().trim()) 
					&& !"".equals(newExtensionsWizzard.getSecondAlert().trim())) {
				limit = ",L(" + newExtensionsWizzard.getTimeLimit() + ":" 
						+ newExtensionsWizzard.getFirstAlert() + ":" + newExtensionsWizzard.getSecondAlert() + ")";
			} 
			
		}

		String wait = "";
		
		if(newExtensionsWizzard.isLimit()) {
			
			wait = "," + newExtensionsWizzard.getTimeWait();				
			
		}

		String app_dial = "SIP/${EXTEN}";
		
		if(newExtensionsWizzard.isTransfer()) {				
			
			if(!"".equals(newExtensionsWizzard.getFirstExtension().trim()) 
					&& "".equals(newExtensionsWizzard.getSecondExtension().trim())) {
				app_dial = "SIP/" + newExtensionsWizzard.getFirstExtension();
			}
			
			if(!"".equals(newExtensionsWizzard.getFirstExtension().trim()) 
					&& !"".equals(newExtensionsWizzard.getSecondExtension().trim())) {
				app_dial = "SIP/" + newExtensionsWizzard.getFirstExtension() + "&" +
						newExtensionsWizzard.getSecondExtension();
			}
			
		}
		
		app_dial += wait + limit;
		
		log.info("app_dial : {}", app_dial);
		
		
		if(!newExtensionsWizzard.isRecord() ) {
			
			answer.setId(new ExtensionsId(context, extension, (byte)1));
			answer.setId_1(id);
			newExtensionsWizzard.getExtensions().add(answer);
			
			dial.setId(new ExtensionsId(context, extension, (byte)2));
			dial.setAppdata(app_dial);
			dial.setId_1(id + 1);
			newExtensionsWizzard.getExtensions().add(dial);
			
			hangup.setId(new ExtensionsId(context, extension, (byte)3));
			hangup.setId_1(id + 2);
			newExtensionsWizzard.getExtensions().add(hangup);
			
//			answer.setExtensionsWizzard(newExtensionsWizzard);
//			dial.setExtensionsWizzard(newExtensionsWizzard);
//			hangup.setExtensionsWizzard(newExtensionsWizzard);
			
		} else {
			
			answer.setId(new ExtensionsId(context, extension, (byte)1));
			answer.setId_1(id);
			newExtensionsWizzard.getExtensions().add(answer);
			
			set.setId(new ExtensionsId(context, extension, (byte)2));
			set.setId_1(id + 1);
			newExtensionsWizzard.getExtensions().add(set);
			
			monitor.setId(new ExtensionsId(context, extension, (byte)3));
			monitor.setId_1(id + 2);
			newExtensionsWizzard.getExtensions().add(monitor);
			
			dial.setId(new ExtensionsId(context, extension, (byte)4));
			dial.setAppdata(app_dial);
			dial.setId_1(id + 3);
			newExtensionsWizzard.getExtensions().add(dial);
			
			hangup.setId(new ExtensionsId(context, extension, (byte)5));
			hangup.setId_1(id + 4);
			newExtensionsWizzard.getExtensions().add(hangup);
			
//			answer.setExtensionsWizzard(newExtensionsWizzard);
//			set.setExtensionsWizzard(newExtensionsWizzard);
//			monitor.setExtensionsWizzard(newExtensionsWizzard);
//			dial.setExtensionsWizzard(newExtensionsWizzard);
//			hangup.setExtensionsWizzard(newExtensionsWizzard);
			
		}
			
		limit = null;
		wait = null;
		app_dial = null;
		
		answer = null;
		set = null;
		monitor = null;
		dial = null;
		hangup = null;
	
		extension = null;
		
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
	
	public ExtensionsWizzard getNewExtensionsWizzard() {
		return newExtensionsWizzard;
	}

	public void setNewExtensionsWizzard(ExtensionsWizzard newExtensionsWizzard) {
		this.newExtensionsWizzard = newExtensionsWizzard;
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

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
		
}