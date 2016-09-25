package com.web.javsterisk.controller;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedProperty;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

import com.ibm.icu.text.MessageFormat;

/**
 * 
 * @author freddycucho
 *
 */
public class BaseController extends SigarCommandBase{
	
	protected final String CLI_HOST = "cli.host";	
	protected final String CLI_USERNAME = "cli.username";	
	protected final String CLI_PASSWORD = "asterisk.host";	
	protected final String CLI_PORT = "cli.port";
	protected final String ASTERISK_CHANNELS = "asterisk.show.channels";		
	protected final String ASTERISK_PEERS = "asterisk.show.peers";
	
	private final String CONTROLLER = "Controller";
	private final String LIST = "ListProducer";
	
	@ManagedProperty("#{msg}")
	private ResourceBundle bundle;
	
	/**
	 * 
	 * @param className
	 * @param stacktrace
	 * @param type
	 * @return String
	 * Param type : 1 = List ; 2 = Controller
	 */
	protected String formatPermission(StringBuilder className, StackTraceElement[] stacktrace, int type){		
		//type : 1- List, 2- Controller
		StackTraceElement st = stacktrace[1];
	    StringBuilder methodName = new StringBuilder(st.getMethodName());	    
		if(className != null){
		    if(type == 1){
		    	className.delete(className.indexOf(LIST), className.indexOf(LIST) + LIST.length());
			} else if (type == 2){				
		    	className.delete(className.indexOf(CONTROLLER), className.indexOf(CONTROLLER) + CONTROLLER.length());		    		
			}
		}
		return className.append(":").append(methodName).toString().toLowerCase();
	}
		
	/**
	 * Return message from ResourceBundle setting in faces-config.xml
	 * @param key
	 * @return String
	 */
	protected String getMessage(String key) {	
		String message = null;
		try {
			message = bundle.getString(key);
		}catch (MissingResourceException e) {
			message = "??"+key+"?? is missing!";
		}
		return message;
	}
	
	/**
	 * Return formatted message from ResourceBundle setting in faces-config.xml
	 * @param key
	 * @param arg
	 * @return String
	 */
	protected String getMessage(String key, Object... arg) {	
		String message = null;
		try {
			 message = MessageFormat.format(bundle.getString(key), arg);
		}catch (MissingResourceException e) {
			message = "??"+key+"?? is missing!";
		}
		return message;
	}


	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public void output(String[] arg0) throws SigarException {
		// TODO Auto-generated method stub
		
	}

}
