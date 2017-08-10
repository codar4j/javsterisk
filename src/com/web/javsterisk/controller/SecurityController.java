package com.web.javsterisk.controller;

import java.io.Serializable;
//import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

//import com.web.asterisk4j.enumeration.RoleType;
import com.web.javsterisk.dao.UserDAO;
import com.web.javsterisk.entity.User;

@ManagedBean
@SessionScoped
public class SecurityController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(SecurityController.class);

	private UserDAO userDAO;
	
	@ManagedProperty("#{localeController}")
	private LocaleController localeController;	
	
	@ManagedProperty("#{themeController}")
	private ThemeController themeController;	
	
	private User user;
	
	private String principal;
	
	private final String URI_SUFIX = ".jsf";
	private final String URI_SEPARATOR = "/";
	
		
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocaleController getLocaleController() {
		return localeController;
	}

	public void setLocaleController(LocaleController localeController) {
		this.localeController = localeController;
	}
	
	public ThemeController getThemeController() {
		return themeController;
	}

	public void setThemeController(ThemeController themeController) {
		this.themeController = themeController;
	}

	@PostConstruct
	public void initUser() {
		userDAO = new UserDAO();
		user = new User();
	}
	
	/*
	public String reLogin(){
		log.debug("not authenticated - relogin...");
		return "login";
	}
	
	public String goHome(){
		log.debug("goHome()...");
		if(isAuthenticated()){
			if(getPrincipal().getRole().equals(RoleType.ADMINISTRADOR)){
				return "dashboard";
			} else {
				return "cdrList";
			}
		} else {
			return "login";
		}
	}
	*/
	
	public String login () throws Exception {		
		log.info("Start Login with username : {}", this.user.getUsername());
		
		String outcome = "DashboardList";
		
		Subject currentUser = SecurityUtils.getSubject();
		
		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest());				
		if(savedRequest != null && savedRequest.getRequestUrl().endsWith(URI_SUFIX)){
			outcome = savedRequest.getRequestUrl().substring(savedRequest.getRequestUrl().lastIndexOf(URI_SEPARATOR) + 1, savedRequest.getRequestUrl().lastIndexOf(URI_SUFIX));
		}			
		
		if(!currentUser.isAuthenticated()){
		
			try {
				
				UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());						
				currentUser.login(token);

				user = userDAO.findByUsername(user.getUsername());
				log.info( "User [{}] logged in successfully.", currentUser.getPrincipal());	
//				sessionScopeController.setLocale(user.getLocale());
				localeController.setLocale(user.getLocale());
				log.info( "Setting Locale in Session : {} - Language : {}", user.getLocale(), user.getLocale() == null ? null : user.getLocale().getDisplayLanguage());
//				sessionScopeController.setTheme(user.getTheme());
				themeController.setTheme(user.getTheme());
				log.info( "Setting Theme in Session : {}", user.getTheme());
//				sessionScopeController.setUser(user);
				log.info( "Setting User in session : {}", user.toString());
				
				if(currentUser.isAuthenticated() && currentUser.hasRole("Estandar") && outcome.equals("DashboardList")){
					outcome = "HomeList";
				}
				
			} catch (UnknownAccountException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("lbl.login.form.error.unknownaccountexception.sumary"), getMessage("lbl.login.form.error.unknownaccountexception.detail")));
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				log.error("Login UnknownAccountException", e);
	        } catch (IncorrectCredentialsException e) {	            
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("lbl.login.form.error.unknownaccountexception.sumary"), getMessage("lbl.login.form.error.unknownaccountexception.detail")));
	        	FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);	
				log.error("Login IncorrectCredentialsException", e);
	        } catch (LockedAccountException e) {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("lbl.login.form.error.unknownaccountexception.sumary"), getMessage("lbl.login.form.error.lockedaccountexception.detail")));
	        	FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				log.error("Login LockedAccountException", e);
	        } catch (AuthenticationException e) {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("lbl.login.form.error.unknownaccountexception.sumary"), getMessage("lbl.login.form.error.authenticationexception.detail")));
	        	FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				log.error("Login AuthenticationException", e);
	        }
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("lbl.login.form.alreadylogin.sumary"), getMessage("lbl.login.form.alreadylogin.detail")));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);        	
			log.info("Login! Ya esta autenticado");
		}
			
		log.debug("outcome : {}", outcome);
		return outcome;
	}
	
	public String logout() throws Exception {
		 Subject currentUser = SecurityUtils.getSubject();	
//		 String principal = currentUser.getPrincipal().toString();
//		 String sessionId = currentUser.getSession(false).getId().toString();
		 try {			 	
	            currentUser.logout();
	            log.info("Logout successfull.");	            
	            return "login";
	        } catch (Exception e) {
	        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("lbl.logout.form.error.exception.sumary"), e.getMessage()));
	        	FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				log.error("Logout error : {}", e.getMessage());
	        }	
		 return null;
	}
	
	/*
	public String login () {		
		log.info("Start Login with username : {}", this.user.getUsername());
		
		String outcome = "home";
				
				User user = userDAO.findByUsername(this.user.getUsername());
				if(user == null || !user.getPassword().equals(this.user.getPassword())){
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("lbl.login.form.error.unknownaccountexception.sumary"), getMessage("lbl.login.form.error.unknownaccountexception.detail")));
					facesContext.getExternalContext().getFlash().setKeepMessages(true);
					log.error("Login UnknownAccount: Usuario o Password no valido");
					outcome = "login";
				} else {					
					log.info( "User [{}] logged in successfully.", user.getUsername());						
					localeController.setLocale(user.getLocale());
					log.info( "Setting Locale from User : {} - Language : {}", localeController.getLocale(), localeController.getLocale().getDisplayLanguage());					
					themeController.setTheme(user.getTheme());
					log.info( "Setting Theme from User : {}", themeController.getTheme());
					HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
					session.setAttribute("principal", user);					
					session.setAttribute("auth", true);
					
					if(user.getRole().equals(RoleType.ADMINISTRADOR)){
						outcome = "dashboard";	
					} else {
						outcome = "cdrList";
					}
					
				}
			
		log.debug("outcome : {}", outcome);
		return outcome;
	}
	
	public String logout () {		
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
		session.setAttribute("principal", "");
		session.setAttribute("principal", null);
		session.removeAttribute("principal");
		session.setAttribute("auth", false);
		session.setAttribute("auth", null);
		session.removeAttribute("auth");
		session.invalidate();
		log.info( "Logout successfully.");
		return "login";
	}
	
	public boolean isAuthenticated() {
		authenticated = false;
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		if(session != null){
			Object obj = session.getAttribute("auth");
			authenticated = obj != null ? (Boolean)obj : false;
		}
		return authenticated;
	}
	*/

	public boolean isAuthenticated(){
		return SecurityUtils.getSubject().isAuthenticated();
	}
	

	public String getPrincipal() {
		principal = SecurityUtils.getSubject().getPrincipal().toString();		
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	
	public boolean isAdministrator() {
		return SecurityUtils.getSubject().hasRole("Administrador");
	}
	
	
}
