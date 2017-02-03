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
	
	private String selectedContext;
	
	private String originalContext;	
	private String originalDigito;
	private int originalLongitud;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Extensions> extensionesFiltered;

	private Extensions newExtensions;
	
	private ExtensionsWizzard newExtensionsWizzard;
				
	private Extensions[] selectedExtensiones;
	
	private Extensions selectedExtension;
	
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
	
	private ExtensionsWizzard makeExtensions(ExtensionsWizzard extensionsWizzard, String context) {
		
		extensionsWizzard.setExtensions(new ArrayList<Extensions>(0));
		
		int id = extensionsDAO.findAllOrderedById().size() + 1;
		
		String extension = StringUtils.rightPad(EXT_PREFIX + extensionsWizzard.getDigito(), extensionsWizzard.getLongitud() + 1, EXT_CHAR);
		
		log.info("-> context : {}", context);
		log.info("-> extesnion generated : {}", extension);		
		
		Extensions answer = new Extensions();
		answer.setApp(APP_ANSWER);		
		
		Extensions set = new Extensions();
		set.setApp(APP_SET);
		set.setAppdata("MONITOR_FILENAME=${STRFTIME($,,%Y%m%-%H%M%S)}-${CALLERID(num)}");
		
//		char c  = (char) 43;
		
		Extensions monitor = new Extensions();
		monitor.setApp(APP_MIX_MONITOR);
		monitor.setAppdata(param_record_path.getValue() + " + ${MONITOR_FILENAME}.wav,b");		
		
		Extensions dial = new Extensions();
		dial.setApp(APP_DIAL);
		
		Extensions hangup = new Extensions();
		hangup.setApp(APP_HANGUP);
		
		String limit = "";
		
		if(extensionsWizzard.isLimit()) {
			
			limit = ",L(" + extensionsWizzard.getTimeLimit() + "::)";
			
			if(!"".equals(extensionsWizzard.getFirstAlert().trim()) 
					&& "".equals(extensionsWizzard.getSecondAlert().trim())) {
				limit = ",L(" + extensionsWizzard.getTimeLimit() + ":" 
					+ extensionsWizzard.getFirstAlert() + ":)";
			}
			
			if(!"".equals(extensionsWizzard.getFirstAlert().trim()) 
					&& !"".equals(extensionsWizzard.getSecondAlert().trim())) {
				limit = ",L(" + extensionsWizzard.getTimeLimit() + ":" 
						+ extensionsWizzard.getFirstAlert() + ":" + extensionsWizzard.getSecondAlert() + ")";
			} 
			
		}

		String wait = "";
		
		if(extensionsWizzard.isWait()) {
			
			wait = "," + extensionsWizzard.getTimeWait();				
			
		}

		String app_dial = "SIP/${EXTEN}";
		
		if(extensionsWizzard.isTransfer()) {				
			
			if(!"".equals(extensionsWizzard.getFirstExtension().trim()) 
					&& "".equals(extensionsWizzard.getSecondExtension().trim())) {
				app_dial = "SIP/" + extensionsWizzard.getFirstExtension();
			}
			
			if(!"".equals(extensionsWizzard.getFirstExtension().trim()) 
					&& !"".equals(extensionsWizzard.getSecondExtension().trim())) {
				app_dial = "SIP/" + extensionsWizzard.getFirstExtension() + "&" +
						extensionsWizzard.getSecondExtension();
			}
			
		}
		
		app_dial += wait + limit;
		
		log.info("app_dial : {}", app_dial);
		
		
		if(!extensionsWizzard.isRecord() ) {
			
			answer.setId(new ExtensionsId(context, extension, (byte)1));
			answer.setId_1(id);
			extensionsWizzard.getExtensions().add(answer);
			
			dial.setId(new ExtensionsId(context, extension, (byte)2));
			dial.setAppdata(app_dial);
			dial.setId_1(id + 1);
			extensionsWizzard.getExtensions().add(dial);
			
			hangup.setId(new ExtensionsId(context, extension, (byte)3));
			hangup.setId_1(id + 2);
			extensionsWizzard.getExtensions().add(hangup);
			
		} else {
			
			answer.setId(new ExtensionsId(context, extension, (byte)1));
			answer.setId_1(id);
			extensionsWizzard.getExtensions().add(answer);
			
			set.setId(new ExtensionsId(context, extension, (byte)2));
			set.setId_1(id + 1);
			extensionsWizzard.getExtensions().add(set);
			
			monitor.setId(new ExtensionsId(context, extension, (byte)3));
			monitor.setId_1(id + 2);
			extensionsWizzard.getExtensions().add(monitor);
			
			dial.setId(new ExtensionsId(context, extension, (byte)4));
			dial.setAppdata(app_dial);
			dial.setId_1(id + 3);
			extensionsWizzard.getExtensions().add(dial);
			
			hangup.setId(new ExtensionsId(context, extension, (byte)5));
			hangup.setId_1(id + 4);
			extensionsWizzard.getExtensions().add(hangup);			
			
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
		
		return extensionsWizzard;
		
	}

	public void register() { 		
		log.info("Start register()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	
//		log.info("Permission module is in list ToDo : {}", permission);
		if ( securityController.isAdministrator() ) {		
			try {
				
				newExtensionsWizzard = makeExtensions(newExtensionsWizzard, context);

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
	
	public void modifier() throws Exception { 
		log.info("Start modifier()");	    
		if ( securityController.isAdministrator() ) {
			ExtensionsWizzard selectedExtensionsWizzard = selectedExtension.getExtensionsWizzard();
			
			if (!originalContext.equals(selectedContext) 
					|| !originalDigito.equals(selectedExtensionsWizzard.getDigito())
					|| originalLongitud != selectedExtensionsWizzard.getLongitud()) {
//				selectedExtensionsWizzard.getExtensions().clear();
				extensionsWizzardDAO.deleter(selectedExtensionsWizzard);
				selectedExtensionsWizzard = makeExtensions(selectedExtensionsWizzard, selectedContext);
				extensionsWizzardDAO.register(selectedExtensionsWizzard);
			} else {
				selectedExtensionsWizzard = makeExtensions(selectedExtensionsWizzard, selectedContext);				
				extensionsWizzardDAO.modifier(selectedExtensionsWizzard);	
			}
			
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
			extensionsWizzardDAO.deleter(selectedExtension.getExtensionsWizzard());
			log.info("Eliminacion exitosa de extension : {}", selectedExtension.getExtensionsWizzard().getId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del usuario : " + selectedExtension.getExtensionsWizzard().getId()));
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

	public Extensions getSelectedExtension() {
		return selectedExtension;
	}

	public void setSelectedExtension(Extensions selectedExtension) {
		this.selectedExtension = selectedExtension;
		this.selectedContext = this.selectedExtension.getId().getContext();
		this.originalContext = this.selectedContext;
		this.originalDigito = this.selectedExtension.getExtensionsWizzard().getDigito();
		this.originalLongitud = this.selectedExtension.getExtensionsWizzard().getLongitud();
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

	public String getSelectedContext() {
		return selectedContext;
	}

	public void setSelectedContext(String selectedContext) {
		this.selectedContext = selectedContext;
	}

	public String getOriginalContext() {
		return originalContext;
	}

	public void setOriginalContext(String originalContext) {
		this.originalContext = originalContext;
	}

	public String getOriginalDigito() {
		return originalDigito;
	}

	public void setOriginalDigito(String originalDigito) {
		this.originalDigito = originalDigito;
	}

	public int getOriginalLongitud() {
		return originalLongitud;
	}

	public void setOriginalLongitud(int originalLongitud) {
		this.originalLongitud = originalLongitud;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
		
}