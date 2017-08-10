package com.web.javsterisk.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author freddycucho
 *
 */
@ManagedBean
public class StringUtil implements Serializable {
	
	private static final Logger log = LogManager.getLogger(StringUtil.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String capitalize(String text) {
		log.debug("Formatting String to capitalize : {}", text);
		return WordUtils.capitalize(text);
	}
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		
		Date d = new Date();
		c.setTimeInMillis(d.getTime());
		System.out.println(d);
		
		System.out.println(c.get(Calendar.YEAR));
		System.out.println(c.get(Calendar.MONTH));
		System.out.println(c.get(Calendar.DATE));
		
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DATE));
		
		String hour = String.valueOf(c.get(Calendar.HOUR));
		String min = String.valueOf(c.get(Calendar.MINUTE));
		String sec = String.valueOf(c.get(Calendar.SECOND));
		
		String fileName =  year + (month.length() == 1 ? "0" + month : month) + (day.length() == 1 ? "0" + day : day) + "-" + (hour.length() == 1 ? "0" + hour : hour) + (min.length() == 1 ? "0" + min : min) + (sec.length() == 1 ? "0" + sec : sec) + "-" + "2001";
		System.out.println(fileName);
		
	}
	
}
