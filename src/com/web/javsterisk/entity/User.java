package com.web.javsterisk.entity;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

//import com.web.asterisk4j.enumeration.RoleType;
//import com.web.econstruct4j.entity.Role;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 
 * @author atorres
 *
 */

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Size(min = 1, max = 10)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String username;

	@NotNull
//	@Size(min = 1, max = 10)	
	@Size(min = 1, max = 255)	
	private String password;
	
	@Transient
	private String password2;
	
	@NotNull
	@Email
	private String email;
	
//	private RoleType role;
	
	@NotNull
	private Locale locale;
	
	@NotNull
	private String theme;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleId", nullable = false)
	@NotNull
	private Role role;
	
	@OneToOne(fetch = FetchType.EAGER)
	private SipBuddies sipBuddies;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public SipBuddies getSipBuddies() {
		return sipBuddies;
	}

	public void setSipBuddies(SipBuddies sipBuddies) {
		this.sipBuddies = sipBuddies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	 
}
