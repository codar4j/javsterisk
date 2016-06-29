package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.UserDAO;
import com.web.javsterisk.entity.User;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class PreferenceController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(PreferenceController.class);

	private UserDAO userDAO;
	
	private User user;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	@ManagedProperty("#{localeController}")
	private LocaleController localeController;	
	
	@ManagedProperty("#{themeController}")
	private ThemeController themeController;	
	
	private Locale locale;
	
	private String theme;
	
	@PostConstruct
	public void initPreference() {
		userDAO = new UserDAO();
		if(securityController.isAuthenticated()){
		//user = securityController.getPrincipal();
		user = userDAO.findByUsername(securityController.getPrincipal());
		log.info("User : {}", user.getName());
		locale = user.getLocale();
		theme = user.getTheme();
		}
	}

	public String modifier() throws Exception { 
		log.info("Start modifier()");    				
		user.setLocale(locale);
		localeController.setLocale(locale);
		log.info("Setting Preference Locale : {}", locale);  
		user.setTheme(theme);
		themeController.setTheme(theme);
		log.info("Setting Preference Theme : {}", theme);
		userDAO.modifier(user);		
		log.info("Modification successful");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		return "preferenceList";
	}


	public Locale getLocale() {
		return locale;
	}


	public void setLocale(Locale locale) {
		this.locale = locale;
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}


	public LocaleController getLocaleController() {
		return localeController;
	}


	public void setLocaleController(LocaleController localeController) {
		this.localeController = localeController;
	}


	public ThemeController getThemeController() {
		return themeController;
	}


	public void setThemeController(ThemeController themeController) {
		this.themeController = themeController;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}