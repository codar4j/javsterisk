package com.web.javsterisk.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.response.CommandResponse;

import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.entity.Call;
import com.web.javsterisk.entity.Channel;
import com.web.javsterisk.entity.Parameter;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class ChannelController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ChannelController.class);

	private ParameterDAO parameterDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
//	private List<Channel> channels;
	
	private List<Call> calls;
	
	private String header;
	
	public void handleChannels() {
		
		log.info("Starting channels processing...");
		
		try {
			
			Parameter channelCmd = parameterDAO.findByName(ASTERISK_CHANNELS);
			
			Parameter host = parameterDAO.findByName(CLI_HOST);
			
			Parameter port = parameterDAO.findByName(CLI_PORT);
			
			Parameter username = parameterDAO.findByName(CLI_USERNAME);
			
			Parameter password = parameterDAO.findByName(CLI_PASSWORD);
			
			ManagerConnectionFactory factory;
			ManagerConnection connection;
			
			factory = new ManagerConnectionFactory(host.getValue(), Integer.parseInt(port.getValue()), username.getValue(), password.getValue());
			connection = factory.createManagerConnection();

			connection.login();
			
			log.info("Asterisk Peers Command : {}", channelCmd.getValue());
			
			CommandAction commandAction = new CommandAction(channelCmd.getValue());			
			CommandResponse resp = (CommandResponse) connection.sendAction(commandAction);
		
//			String[] response = new String[9];
//			
//			response[0] = "Channel              Location            		 State   Application(Data)             ";
//			response[1] = "SIP/2002-00000005    (None)              		 Up      AppDial((Outgoing Line))      ";
//			response[2] = "SIP/2003-00000009    (None)              		 Up      AppDial((Outgoing Line))      ";
//			response[3] = "SIP/2004-00000008    2003@from-sip:1 	 Up      Dial(SIP/2003)                ";
//			response[4] = "SIP/2001-00000004    2002@from-sip:1      	 Up      Dial(SIP/2002)                ";
//			response[5] = "SIP/2005-00000002    5001@from-sip:1      Up      Playback(hello-world)         ";
//			response[6] = "4 active channels";
//			response[7] = "2 active calls";
//			response[8] = "5 calls processed";
			
			List<String> responses = resp.getResult();
			String[] response = responses.toArray(new String[responses.size()]);
			
			connection.logoff();
			
			List<Channel> channels = new ArrayList<Channel>(0);
			HashMap<String, Channel> channelMap = new HashMap<String, Channel>();
			
			for(int i = 1; i < response.length - 3; i++){			
				StringTokenizer st = new StringTokenizer(response[i]);
				String[] _channels = new String[st.countTokens()];
				int idx = 0;
				while (st.hasMoreElements()) {			
					_channels[idx] = (String) st.nextElement();
					idx++;
				}
				Channel channel = null;				
				
				if(_channels.length == 4){
					//dasactivado		
					channel = new Channel();
					channel.setExtensionSource(formatExtensionSource(_channels[0]));
					channel.setChannel(_channels[0]);
					channel.setExtensionTarget(formatExtensionTarget(_channels[1]));
					channel.setLocation(_channels[1]);
					channel.setState(_channels[2]);
					channel.setApplicationData(_channels[3]);
				} else if(_channels.length == 5){
					channel = new Channel();
					channel.setExtensionSource(formatExtensionSource(_channels[0]));
					channel.setChannel(_channels[0]);
					channel.setExtensionTarget(formatExtensionTarget(_channels[1]));
					channel.setLocation(_channels[1]);
					channel.setState(_channels[2]);
					channel.setApplicationData(_channels[3] + " " + _channels[4]);
				}
				
				if(channel != null){
					if(channel.getState().trim().equalsIgnoreCase("UP")){					
						channels.add(channel);
					}
					channelMap.put(channel.getExtensionSource(), channel);
				}
			}
			
			calls = new ArrayList<Call>(0);
			int ind = 1;
			for(Channel chn : channels){
				if(chn.getState().trim().equalsIgnoreCase("UP") && !chn.getLocation().trim().equalsIgnoreCase("(NONE)") && channelMap.containsKey(chn.getExtensionTarget())){
					Call call = new Call();
					chn.setSource(true);
					call.setSource(chn);
					Channel target = channelMap.get(chn.getExtensionTarget());
					target.setSource(false);
					call.setTarget(target);
					call.setActive(true);
					call.setId(ind);
					calls.add(call);
					ind++;
				}
			}
			
			if(response.length > 3){
				header = response[response.length - 3] + " - " + response[response.length - 2] + " - " + response[response.length - 1];
			}
			
		} catch (IllegalStateException e) {
			log.error("IllegalStateException", e);
		}
		catch (IOException e) {
			log.error("IOException", e);
		} catch (AuthenticationFailedException e) {
			log.error("AuthenticationFailedException", e);
		} catch (TimeoutException e) {
			log.error("TimeoutException", e);
		}
		
	}
	
	private String formatExtensionTarget(String channel) {
		if(channel != null && !channel.trim().equals("") && !channel.trim().equalsIgnoreCase("(NONE)")){
			return channel.substring(0, channel.indexOf("@"));
		}
		return null;
	}

	private String formatExtensionSource(String channel) {	
		if(channel != null && !channel.trim().equals("")){
			return channel.substring(channel.indexOf("/") + 1, channel.indexOf("-"));	
		}
		return null;
	}

	@PostConstruct
	public void initChannels() {
		parameterDAO = new ParameterDAO();
		if(securityController.isAuthenticated()){
			handleChannels();
		}
	}
	
	public List<Call> getCalls() {
		return calls;
	}

	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
//	public static void main(String[] args) {
//		new ChannelController().handleChannels();
//	}
	
}