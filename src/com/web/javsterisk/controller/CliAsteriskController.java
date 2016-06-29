package com.web.javsterisk.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Parameter;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class CliAsteriskController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(CliAsteriskController.class);

	private ParameterDAO parameterDAO;
	
	@PostConstruct
	public void initNewCliAsterisk () {
		parameterDAO = new ParameterDAO();
	}
	
	public String handleCommand(String command, String[] params) {
		
		StringBuilder output = new StringBuilder();
		StringBuilder asterisk_command = new StringBuilder();
		try {
			
			if(command == null || command.trim().equals("")){
				return "";
			}
			
			if(command.equals("clear")){
				
				PushContext pushContext = PushContextFactory.getDefault().getPushContext();
				pushContext.push("/clearTerminal", "Pressed"); 
				return null;
				
			} else {
			
				ManagerConnectionFactory factory;
				ManagerConnection connection;
	
				Parameter host = parameterDAO.findByName(CLI_HOST);
				
				Parameter port = parameterDAO.findByName(CLI_PORT);
				
				Parameter username = parameterDAO.findByName(CLI_USERNAME);
				
				Parameter password = parameterDAO.findByName(CLI_PASSWORD);
				
				factory = new ManagerConnectionFactory(host.getValue(), Integer.parseInt(port.getValue()), username.getValue(), password.getValue());
				connection = factory.createManagerConnection();
	
				connection.login();
				
				asterisk_command.append(command);
				
				for(int i = 0; i < params.length; i++){
					asterisk_command.append(" ").append(params[i]);
				}
				
				log.info("Terminal Command : {}", asterisk_command.toString());
				
				CommandAction commandAction = new CommandAction(asterisk_command.toString());
				
				CommandResponse response = (CommandResponse) connection.sendAction(commandAction);
			
				output.append("<pre>");
//				int i = 1;
				for (String line : response.getResult())
				{
//					log.info("response line {} : {}", i , line);
					output.append(line).append("\r");
//					i++;
//					output.append("<pre>Demo").append("\r").append(" Alex</pre>");
				}
				
				output.append("</pre>");
	
				connection.logoff();
			
		}

		} catch (IllegalStateException e) {
			output.append(e).append("\n");		
			log.error("IllegalStateException", e);
		} catch (IOException e) {
			output.append(e).append("\n");
			log.error("IOException", e);
		} catch (AuthenticationFailedException e) {
			output.append(e).append("\n");
			log.error("AuthenticationFailedException", e);
		} catch (TimeoutException e) {
			output.append(e).append("\n");
			log.error("TimeoutException", e);			
		}		 
		
		log.info("output : {}", output.toString());
		return output.toString();
		  
    }
	
}