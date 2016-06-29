package com.web.javsterisk.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import com.web.javsterisk.entity.Parameter;
import com.web.javsterisk.entity.Peer;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class PeerController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(PeerController.class);

	private ParameterDAO parameterDAO;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private List<Peer> peers;
	
	private List<Peer> peersFiltered;
	
	private String header;
	
	public void handlePeers() {
		
		log.info("Starting peers processing...");
		
		try {
			
			Parameter peerCmd = parameterDAO.findByName(ASTERISK_PEERS);
			
			Parameter host = parameterDAO.findByName(CLI_HOST);
			
			Parameter port = parameterDAO.findByName(CLI_PORT);
			
			Parameter username = parameterDAO.findByName(CLI_USERNAME);
			
			Parameter password = parameterDAO.findByName(CLI_PASSWORD);
			
			ManagerConnectionFactory factory;
			ManagerConnection connection;
			
			factory = new ManagerConnectionFactory(host.getValue(), Integer.parseInt(port.getValue()), username.getValue(), password.getValue());
			connection = factory.createManagerConnection();

			connection.login();
			
			log.info("Asterisk Peers Command : {}", peerCmd.getValue());
			
			CommandAction commandAction = new CommandAction(peerCmd.getValue());			
			CommandResponse resp = (CommandResponse) connection.sendAction(commandAction);
		
//			String[] response = new String[6];
//			
//			response[0] = "Name/username              Host                                    Dyn Forcerport 	ACL 	Port     	Status 			    Realtime";
//			response[1] = "2001/2001                  192.168.107.169                          D   N                               1077    OK (101 ms) 		Cached RT";
//			response[2] = "2002/2002                  192.168.107.162                          D   N                               1037    OK (101 ms) 		Cached RT";
//			response[3] = "2003/2003                  192.168.107.1                              D   N                               59435  OK (105 ms) 		Cached RT";
//			response[4] = "2004/2004                  (Unspecified)                                D   N                                     0    UNKNOWN 		Cached RT";
//			response[5] = "4 sip peers [Monitored: 3 online, 1 offline Unmonitored: 0 online, 0 offline]";
//			
			
			List<String> responses = resp.getResult();
			String[] response = responses.toArray(new String[responses.size()]);
			
			connection.logoff();
			
			peers = new ArrayList<Peer>(0);
			
			for(int i = 1; i < response.length - 1; i++){			
				StringTokenizer st = new StringTokenizer(response[i]);
				String[] _peers = new String[st.countTokens()];
				int idx = 0;
				while (st.hasMoreElements()) {			
					_peers[idx] = (String) st.nextElement();
					idx++;
				}
				Peer peer = null;				
				
				if(_peers.length == 5){
					//No registrados
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setPort(_peers[3]);
					peer.setStatus(_peers[4]);
				} else if(_peers.length == 7){
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setPort(_peers[3]);
					peer.setStatus(_peers[4] + " " + _peers[5] + " " + _peers[6]);
				} else if(_peers.length == 8){
					//dasactivado
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setForceport(_peers[3]);
					peer.setPort(_peers[4]);
					peer.setStatus(_peers[5]);
					peer.setRealtime(_peers[6] + " " + _peers[7]);
				} else if(_peers.length == 9){
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setForceport(_peers[3]);
					peer.setACL(_peers[4]);
					peer.setPort(_peers[5]);
					peer.setStatus(_peers[6]);
					peer.setRealtime(_peers[7] + " " + _peers[8]);
				} else if(_peers.length == 10){			
					//activado					
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setForceport(_peers[3]);
					peer.setPort(_peers[4]);
					peer.setStatus(_peers[5] + " " + _peers[6] + " " + _peers[7]);
					peer.setRealtime(_peers[8] + " " + _peers[9]);
				} else if (_peers.length == 11){
					peer = new Peer();
					peer.setName(_peers[0]);
					peer.setHost(_peers[1]);
					peer.setDyn(_peers[2]);
					peer.setForceport(_peers[3]);
					peer.setACL(_peers[4]);
					peer.setPort(_peers[5]);
					peer.setStatus(_peers[6] + " " + _peers[7] + " " + _peers[8]);
					peer.setRealtime(_peers[9] + " " + _peers[10]);
				}
				
				if(peer != null){
					peer.setId(i);
					peers.add(peer);
				}
			}
			
			if(response.length > 1){
				header = response[response.length - 1];
			}
			
		} catch (IllegalStateException e) {
			log.error("IllegalStateException", e);
		} catch (IOException e) {
			log.error("IOException", e);
		} catch (AuthenticationFailedException e) {
			log.error("AuthenticationFailedException", e);
		} catch (TimeoutException e) {
			log.error("TimeoutException", e);
		}
		
	}
	
	@PostConstruct
	public void initPeers() {
		parameterDAO = new ParameterDAO();
		if(securityController.isAuthenticated()){
		handlePeers();
		}
	}
	
	public List<Peer> getPeers() {		
		return peers;
	}

	public void setPeers(List<Peer> peers) {
		this.peers = peers;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<Peer> getPeersFiltered() {
		return peersFiltered;
	}

	public void setPeersFiltered(List<Peer> peersFiltered) {
		this.peersFiltered = peersFiltered;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}
	
}