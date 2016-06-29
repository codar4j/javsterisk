package com.web.javsterisk.controller;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.web.javsterisk.dao.CdrDAO;
import com.web.javsterisk.dao.ParameterDAO;
import com.web.javsterisk.dao.UserDAO;
import com.web.javsterisk.entity.Cdr;
import com.web.javsterisk.entity.Parameter;
import com.web.javsterisk.entity.User;
import com.web.javsterisk.enumeration.CallStatus;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class CdrController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(CdrController.class);
	
	private CdrDAO cdrDAO;
	
	private UserDAO userDAO;
		
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private ParameterDAO parameterDAO;
	
	private List<Cdr> cdrsFiltered;
	
	private List<Cdr> cdrs;

	private Cdr newCdr;
				
	private Cdr[] selectedCdrs;
	
	private int selectedCdrsSize;
	
	private Date since;
	
	private Date to;
	
	private SelectItem[] cdrsFilter;
	
	Parameter recorderUrl;
	
	Parameter recorderPath;
	
	Parameter recorderExt;
	
	@PostConstruct
	public void initNewCdr() {
		
		cdrDAO = new CdrDAO();
		userDAO = new UserDAO();
		parameterDAO = new ParameterDAO();
		
		if(securityController.isAuthenticated()){
		
		if(since == null & to == null){
			Calendar _since = Calendar.getInstance();
			_since.set(Calendar.HOUR_OF_DAY, 0);
			_since.set(Calendar.MINUTE, 0);
			_since.set(Calendar.SECOND, 0);
			_since.set(Calendar.MILLISECOND, 0);
			
			Calendar _to = Calendar.getInstance();
			_to.set(Calendar.HOUR_OF_DAY, 23);
			_to.set(Calendar.MINUTE, 59);
			_to.set(Calendar.SECOND, 59);
			_to.set(Calendar.MILLISECOND, 999);			
			
			since = new Date(_since.getTimeInMillis());
			to = new Date(_to.getTimeInMillis());
		}
		
		cdrsFilter = new SelectItem[] {
			   	new SelectItem("", "Seleccione"), 
			   	new SelectItem(CallStatus.ANSWERED.getValue(), CallStatus.ANSWERED.getValue()),
			   	new SelectItem(CallStatus.NOANSWER.getValue(), CallStatus.NOANSWER.getValue())};	
		
		searchByDate();
		}
	}
	
	public void searchByDate() {
		Calendar _since = Calendar.getInstance();
		_since.setTimeInMillis(since.getTime());
		_since.set(Calendar.HOUR_OF_DAY, 0);
		_since.set(Calendar.MINUTE, 0);
		_since.set(Calendar.SECOND, 0);
		_since.set(Calendar.MILLISECOND, 0);
		
		Calendar _to = Calendar.getInstance();
		_to.setTimeInMillis(to.getTime());
		_to.set(Calendar.HOUR_OF_DAY, 23);
		_to.set(Calendar.MINUTE, 59);
		_to.set(Calendar.SECOND, 59);
		_to.set(Calendar.MILLISECOND, 999);
		
		since = new Date(_since.getTimeInMillis());
		to = new Date(_to.getTimeInMillis());
		
		log.info("sarting searchByDate()...");
		log.debug("since : {}", since);
		log.debug("to : {}", to);	
		
		if(securityController.isAdministrator()){
			cdrs = cdrDAO.findAllByDateOrderedById(since, to);	
		} else {
			User user = userDAO.findByUsername(securityController.getPrincipal());
			cdrs = cdrDAO.findAllByDateOrderedById(since, to, user.getSipBuddies().getName());
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		recorderUrl = parameterDAO.findByName("asterisk.recorder.url");
		recorderPath = parameterDAO.findByName("asterisk.recorder.path");
		recorderExt = parameterDAO.findByName("asterisk.recorder.ext");
		
		int i = 0;
		for(Cdr cdr : cdrs){
			
			cdr.setRecorder(generateRecorderUrl(cdr.getCalldate(), cdr.getSrc(), cdr.getDisposition()));
			
			Float s = cdr.getBillsec();			
			Float h = s / 3600;  
			int hora = h.intValue();			
		    Float m = (s - (hora * 3600)) / 60;
		    int minute = m.intValue();		    
		    Float seg = s - ((hora * 3600) + (minute * 60));			    
	        cdr.setDurationTime(hora + "h " + minute + "m " + df.format(seg) + "s");
	        cdrs.set(i, cdr);
	        i++;
		}
	}
	
	private String generateRecorderUrl(Date callDate, String src, String status) {		
		if(!status.equals("ANSWERED")){
			return "";
		}
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(callDate.getTime());

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DATE));		
		String hour = String.valueOf(c.get(Calendar.HOUR));
		String min = String.valueOf(c.get(Calendar.MINUTE));
		String sec = String.valueOf(c.get(Calendar.SECOND));
		
		String fileName = year + (month.length() == 1 ? "0" + month : month) + (day.length() == 1 ? "0" + day : day) + "-" + (hour.length() == 1 ? "0" + hour : hour) + (min.length() == 1 ? "0" + min : min) + (sec.length() == 1 ? "0" + sec : sec) + "-" + src + "." + recorderExt.getValue();
		
		String fileNamePath = recorderPath.getValue() + fileName;
		
		log.info("Recorder File : {}", fileNamePath);
		
		File file = new File(fileNamePath);
		
		if(!file.exists()){
			return "";
		}
		
		String url = recorderUrl.getValue() + fileName;
		
		log.info("Recorder File URL : {}", url);
		
		return url;
	}
	
	public List<Cdr> getCdrsFiltered() {
		return cdrsFiltered;
	}

	public void setCdrsFiltered(List<Cdr> cdsrFiltered) {
		this.cdrsFiltered = cdsrFiltered;
	}

	public Cdr getNewCdr() {
		return newCdr;
	}

	public void setNewCdr(Cdr newCdr) {
		this.newCdr = newCdr;
	}

	public Cdr[] getSelectedCdrs() {
		return selectedCdrs;
	}

	public void setSelectedCdrs(Cdr[] selectedCdrs) {
		this.selectedCdrs = selectedCdrs;
	}

	public int getSelectedCdrsSize() {
		selectedCdrsSize = this.selectedCdrs == null ? 0 : this.selectedCdrs.length;
		return selectedCdrsSize;
	}

	public void setSelectedCdrsSize(int selectedCdrsSize) {
		this.selectedCdrsSize = selectedCdrsSize;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public List<Cdr> getCdrs() {
		return cdrs;
	}

	public void setCdrs(List<Cdr> cdrs) {
		this.cdrs = cdrs;
	}
	
	public SelectItem[] getCdrsFilter() {
		return cdrsFilter;
	}

	public void setCdrsFilter(SelectItem[] cdrsFilter) {
		this.cdrsFilter = cdrsFilter;
	}

	public Parameter getRecorderUrl() {
		return recorderUrl;
	}

	public void setRecorderUrl(Parameter recorderUrl) {
		this.recorderUrl = recorderUrl;
	}

	public Parameter getRecorderPath() {
		return recorderPath;
	}

	public void setRecorderPath(Parameter recorderPath) {
		this.recorderPath = recorderPath;
	}

	public Parameter getRecorderExt() {
		return recorderExt;
	}

	public void setRecorderExt(Parameter recorderExt) {
		this.recorderExt = recorderExt;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
	}	
	
}