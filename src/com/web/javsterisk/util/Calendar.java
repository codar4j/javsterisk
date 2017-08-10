package com.web.javsterisk.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * 
 * @author atorres
 *
 */
@ManagedBean
@RequestScoped
public class Calendar {
	
	private Date currentDate = new Date();

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public static String parseString(Date date, String format){
		Format f = new SimpleDateFormat(format);
		return f.format(date);
	}
	
}