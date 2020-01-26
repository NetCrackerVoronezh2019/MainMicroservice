package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

import Jacson.Views;




@Entity
@Table(name = "USERS")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="USERID")
	private Long userId;
	
	@Column(name="ISACTIVATE")
	@JsonView(Views.UserInfoForChangeProps.class)
	public boolean IsActivate;
	
	@Column(name="ACTIVATECODE")
	private String activateCode;
	
	@Size(min=4, max=20)
	@Column(name="FIRSTNAME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String firstname;
	
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="LASTNAME")
	@Size(min=4, max=20)
	private String lastname;
	
	@Email
	@Column(name="EMAIL")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String email;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Max(90)
	@Column(name="AGE")
	private int age;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVICEWEBSITE", nullable = false)
    private Service service;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonView(Views.UserInfoForChangeProps.class)
    @JoinColumn(name = "ROLEID", nullable = false)
    private Role role ;
	
	 
	public Role getRole() {
		return this.role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	 
	 
	public long getUserid() {
		return userId;
	}
	 
	public void setUserid(long userid) {
		userId = userid;
	}

	public boolean isIsActivate() {
		return IsActivate;
	}
	
	public void setIsActivate(boolean isActivate) {
		IsActivate = isActivate;
	}
	 
	public String getActivateCode() {
		return activateCode;
	}
	
	public void setActivateCode(String activateCode) {
		this.activateCode = activateCode;
	}
	
	public String getFirstname() {
		return firstname;
	}
	 
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	 
	public String getLastname() {
		return lastname;
	}
	 
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	 
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	 
	public void setPassword(String password) {
		this.password = password;
	}
	 
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getService() {
		if(this.service==null)
		 {
			return "none";
		 }
		return this.service.getServiceWebSite();
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	

}
