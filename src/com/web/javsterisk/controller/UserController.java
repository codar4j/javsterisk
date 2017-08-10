package com.web.javsterisk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.exception.ConstraintViolationException;

import com.web.javsterisk.dao.RoleDAO;
import com.web.javsterisk.dao.UserDAO;
import com.web.javsterisk.entity.Role;
import com.web.javsterisk.entity.User;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation

@ManagedBean
@ViewScoped
public class UserController extends BaseController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(UserController.class);
	
	private UserDAO userDAO;
	private RoleDAO roleDAO;
	
	@ManagedProperty("#{localeController}")
	private LocaleController localeController;
	
	@ManagedProperty("#{themeController}")
	private ThemeController themeController;
	
	@ManagedProperty("#{securityController}")
	private SecurityController securityController;
	
	private String passwordEdit;
	private String password2Edit;
	
	private List<User> usersFiltered;

	private User newUser;
				
	private User[] selectedUsers;
	
	private int selectedUsersSize;
	
	private List<User> users; 
	
	private List<Role> roles;
	
	private SelectItem[] rolesFilter; 
	
	@PostConstruct
	public void initNewUser() {
		userDAO = new UserDAO();		
		if(securityController.isAuthenticated()){
			
			roleDAO = new RoleDAO();
			
			   roles = new ArrayList<Role>(0);
			   
			   roles = roleDAO.findAllOrderedByName();
			   
			   rolesFilter = new SelectItem[roles.size() + 1];
			   rolesFilter[0] = new SelectItem("", "Seleccione"); 
			   for(int i = 1 ; i < rolesFilter.length; i ++) {
				   rolesFilter[i] = new SelectItem(roles.get(i-1).getName(), roles.get(i-1).getName());
			   }
			   
//			   rolesFilter = new SelectItem[] {
//					   	new SelectItem("", "Seleccione"), 
//					   	new SelectItem(RoleType.ADMINISTRADOR, RoleType.ADMINISTRADOR.toString()),
//					   	new SelectItem(RoleType.ESTANDAR, RoleType.ESTANDAR.toString())};
			
			users = userDAO.findAllOrderedByUsername();
			newUser = new User();		
			Locale defaultLocale =  localeController.getDefaultLocale();
			log.info("Setting in new User Default Locale : {} - {}", defaultLocale, defaultLocale.getDisplayLanguage()); 
			newUser.setLocale(defaultLocale);		
			String defaultTheme = themeController.getDefaultTheme();
			log.info("Setting in new User Default Theme : {}", defaultTheme);		
			newUser.setTheme(defaultTheme);
		}
	}
	
	public void register() { 		
		log.info("Start register()");
		if ( securityController.isAdministrator() ) {		
			try {
				Sha256Hash sha256Hash = new Sha256Hash(newUser.getPassword());//hashing password
				newUser.setPassword(sha256Hash.toHex());
				userDAO.register(newUser);			
				log.info("Registration successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));			
				initNewUser();
			} catch (ConstraintViolationException e) {
				log.error("Exception", e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El usuario ya existe"));
			} catch (Exception e) {
				log.error("Exception", e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}		
	}

	public void modifier() { 
		log.info("Start modifier()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {
			try {
				User selectedUser = selectedUsers[0];	
				if(passwordEdit != null && !passwordEdit.trim().equalsIgnoreCase("")){	
					Sha256Hash sha256Hash = new Sha256Hash(passwordEdit);//hashing password
					selectedUser.setPassword(sha256Hash.toHex());
//					selectedUser.setPassword(passwordEdit);	
				}	
				userDAO.modifier(selectedUser);
				log.info("Modification successful");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modified!", "Modification successful"));
				initNewUser();
			} catch (Exception e) {				
				if(e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					log.error("ConstraintViolationException", e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El usuario ya existe"));
				} else {
					log.error("Exception", e);
				}
			} 
		} else {
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public void deleter() throws Exception { 
		log.info("Start deleter()");
//		String permission = formatPermission(new StringBuilder(this.getClass().getSimpleName()), Thread.currentThread().getStackTrace(), 2);	    
		if ( securityController.isAdministrator() ) {		
			for(User selectedUser : selectedUsers){				
				userDAO.deleter(selectedUser);
				log.info("Eliminacion exitosa del usuario : {}", selectedUser.getUsername());
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Eliminacion exitosa del usuario : " + selectedUser.getUsername()));
			}
			initNewUser();      
		} else {			
//			log.error("You have not privileges for this ACL {}", permission);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization!", "No tiene privilegios para esta accion"));
		}
	}

	public List<User> getUsersFiltered() {
		return usersFiltered;
	}

	public void setUsersFiltered(List<User> usersFiltered) {
		this.usersFiltered = usersFiltered;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public User[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(User[] selectedUsers) {
		this.selectedUsers = selectedUsers;				
	}

	public int getSelectedUsersSize() {
		selectedUsersSize = this.selectedUsers == null ? 0 : this.selectedUsers.length;
		return selectedUsersSize;
	}

	public void setSelectedUsersSize(int selectedUsersSize) {
		this.selectedUsersSize = selectedUsersSize;
	}

	public String getPasswordEdit() {
		return passwordEdit;
	}

	public void setPasswordEdit(String passwordEdit) {
		this.passwordEdit = passwordEdit;
	}

	public String getPassword2Edit() {
		return password2Edit;
	}

	public void setPassword2Edit(String password2Edit) {
		this.password2Edit = password2Edit;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public SelectItem[] getRolesFilter() {
		return rolesFilter;
	}

	public void setRolesFilter(SelectItem[] rolesFilter) {
		this.rolesFilter = rolesFilter;
	}

	public SecurityController getSecurityController() {
		return securityController;
	}

	public void setSecurityController(SecurityController securityController) {
		this.securityController = securityController;
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
	
}