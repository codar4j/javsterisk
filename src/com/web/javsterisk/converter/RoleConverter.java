package com.web.javsterisk.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.RoleDAO;
import com.web.javsterisk.entity.Role;

@FacesConverter(value = "com.web.javsterisk.converter.RoleConverter")
public class RoleConverter implements Converter {

	private static final Logger log = LogManager.getLogger(RoleConverter.class);
	
	private static final String ATTRIBUTE_ENUM_TYPE = "RoleConverter.name";
	
	private RoleDAO roleDAO;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Class<Role> roleType = (Class<Role>) component.getAttributes().get(ATTRIBUTE_ENUM_TYPE);
		try {
			log.info("RoleType: {} value : {}", roleType, value);
			roleDAO = new RoleDAO();
			Role role = roleDAO.findByName(value);
			roleDAO = null;
			return role;
		} catch (IllegalArgumentException e) {
			throw new ConverterException(new FacesMessage("Value is not an role of type: " + roleType));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		log.info("Object value : {}", value);
	    if (value instanceof Role) {
	        component.getAttributes().put(ATTRIBUTE_ENUM_TYPE, ((Role)value).getClass());
	        log.info("Role name : {}", ((Role)value).getName());
	        return ((Role) value).getName();
	    } else if(value != null) {	    	
	        throw new ConverterException(new FacesMessage("Value is not an enum: " + ((Role)value).getClass()));
	    } else {
	    	return null;
	    }
	}

}
