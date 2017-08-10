package com.web.javsterisk.converter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.controller.UserController;

@ManagedBean
@ViewScoped
public class EntityConverter implements Converter, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(EntityConverter.class);
	
	final private Map<String, Object> converterMap = new HashMap<String, Object>();
	final private Map<Object, String> reverseConverterMap = new HashMap<Object, String>();
		
	private int incrementor = 1;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String uuid) {
		Object object = this.converterMap.get(uuid);
//		log.debug("Converting getAsObject... String value : {}, Returning : {}", uuid, object);
		return object;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object entity) {
		String object = "";
		
//		if (this.conversation.isTransient()) {
//			log.debug("Conversion attempted without a long running conversation");
//		}
		
		if (this.reverseConverterMap.containsKey(entity)) {
			object = this.reverseConverterMap.get(entity);			
		} else {
			final String incrementorStringValue = String.valueOf(this.incrementor++);
			this.converterMap.put(incrementorStringValue, entity);
			this.reverseConverterMap.put(entity, incrementorStringValue);
			object = incrementorStringValue;
		}
//		log.debug("Converting getAsString... Object value : {}, Returning : {}", entity, object);
		return object;
	}

}