package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author freddycucho
 *
 */
@ManagedBean
@SessionScoped
public class LocaleController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(LocaleController.class);
	
	private Locale locale;
	
	private Locale defaultLocale;	
	
	private List<Locale> supportedLocales;
	
	private SelectItem[] localeFilter;
	
	@PostConstruct
	public void initLocale() {
//		if(securityController.isAuthenticated()){
		
		localeFilter = new SelectItem[] {new SelectItem("", "Seleccione"), new SelectItem("true", "Activo"), new SelectItem("false", "Inactivo")};
		
		supportedLocales = new ArrayList<Locale>();
		Iterator<Locale> it = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
		while(it.hasNext()){
			supportedLocales.add((Locale)it.next());
		}
		
		if(supportedLocales.size() > 0) {
			localeFilter = new SelectItem[supportedLocales.size() + 1];
			localeFilter[0] = new SelectItem("", "Seleccione");
			int i = 1;
			for(Locale _locale : supportedLocales){
				localeFilter[i] = new SelectItem(WordUtils.capitalize(_locale.getDisplayLanguage()), WordUtils.capitalize(_locale.getDisplayLanguage()));
				i++;
			}
		}
		
		
		defaultLocale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
		log.debug("Loading Default Locale : {}", defaultLocale);
		if(locale == null){			
			locale = defaultLocale;//facesContext.getViewRoot().getLocale();
			log.debug("Locale is null loading from current view : {}", locale);
		}
		log.debug("Loading Locale : {}", locale);		
//		}
	}
	
	public Locale getLocale() {		
		return locale;
	}

	public void setLocale(Locale locale) {		
		this.locale = locale;
	}

	public Locale getDefaultLocale() {		
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public List<Locale> getSupportedLocales() {
		return supportedLocales;
	}

	public void setSupportedLocales(List<Locale> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}

	public SelectItem[] getLocaleFilter() {
		return localeFilter;
	}

	public void setLocaleFilter(SelectItem[] localeFilter) {
		this.localeFilter = localeFilter;
	}
	
}
