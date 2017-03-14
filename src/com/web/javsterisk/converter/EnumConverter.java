package com.web.javsterisk.converter;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@FacesConverter(value = "com.web.javsterisk.converter.EnumConverter")
public class EnumConverter implements Converter {

	private static final Logger log = LogManager.getLogger(EnumConverter.class);
	
	private static final String ATTRIBUTE_ENUM_TYPE = "EnumConverter.enumType";
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Class<Enum> enumType = (Class<Enum>) component.getAttributes().get(ATTRIBUTE_ENUM_TYPE);
		try {
			log.info("enumType: {} value : {}", enumType, value);
//			log.info("CarType.valueOf(value): " + CarType.valueOf(value));
			log.info("Enum.valueOf(enumType, value): {}", Enum.valueOf(enumType, value));
			return Enum.valueOf(enumType, value);
		} catch (IllegalArgumentException e) {
			throw new ConverterException(new FacesMessage("Value is not an enum of type: " + enumType));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		log.info("value : {}", value);
	    if (value instanceof Enum) {
	        component.getAttributes().put(ATTRIBUTE_ENUM_TYPE, ((Enum)value).getDeclaringClass());
	        return ((Enum<?>) value).name();
	    } else if(value != null) {	    	
	        throw new ConverterException(new FacesMessage("Value is not an enum: " + ((Enum)value).getDeclaringClass()));
	    } else {
	    	return null;
	    }
	}

}
