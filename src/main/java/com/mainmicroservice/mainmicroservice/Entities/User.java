package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.*;
import javax.validation.constraints.*;



@Entity
@Table(name = "USERS")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="USERID")
	private long Userid;
	@Column(name="ISACTIVATE")
	public boolean IsActivate;
	@Column(name="ACTIVATECODE")
	private String activateCode;
	@Size(min=4, max=20)
	@Column(name="FIRSTNAME")
	private String firstname;
	@Column(name="LASTNAME")
	@Size(min=4, max=20)
	private String lastname;
	@Email
	@Column(name="EMAIL")
	private String email;
	@Column(name="PASSWORD")
	private String password;
	@Max(90)
	@Column(name="AGE")
	private int age;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVICEWEBSITE", nullable = false)
    private Service service;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLEID", nullable = false)
    private Role role ;
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public long getUserid() {
		return Userid;
	}
	public void setUserid(long userid) {
		Userid = userid;
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
