package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.*;
import javax.validation.constraints.*;


/*
 CREATE TABLE Users
(
  UserId 	SERIAL PRIMARY KEY,
  FirstName CHARACTER VARYING(30),
  LastName CHARACTER VARYING(30),
  Email CHARACTER VARYING(30),
	Password CHARACTER VARYING(30),
	ServiceWebSite CHARACTER VARYING(30),
  Age INTEGER,
	ActivatedCode CHARACTER VARYING(100),
	IsActivate BOOLEAN,
	FOREIGN KEY (ServiceWebSite) REFERENCES Services (ServiceWebSite) ON DELETE CASCADE
);
 */


@Entity
@Table(name = "Users")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Userid;
	public boolean isActivate;
	private String activateCode;
	@Size(min=4, max=20)
	private String firstname;
	@Size(min=4, max=20)
	private String lastname;
	@Email
	private String email;
	private String password;
	@Max(90)
	private int age;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICEWEBSITE", nullable = false)
    private Services service;
	
	public String getFirstname()
	{
		return this.firstname;
	}
	public void setFirstname(String x)
	{
		this.firstname=x;
	}
	public String getLastName()
	{
		return this.lastname;
	}
	
	public void setLastname(String x)
	{
		this.lastname=x;
	}
	public String getPassword()
	
	{
		return this.password;
	}
	public void setPassword(String x)
	{
		this.password=x;
	}
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(String x)
	{
		this.email=x;
	}
	
 public int getAge()
 {
	 return 100;
 }
	public String getService()
	{
		if(this.service==null)
		{
			return "null";
		}
		
		return service.getServiceWebSite();
	}
	

	public boolean isActivate() {
		return isActivate;
	}
	
	public void setActivate(boolean isActivate) {
		this.isActivate = isActivate;
	}
	
	public String getActivateCode() {
		return activateCode;
	}
	
	public void setActivateCode(String activateCode) {
		this.activateCode = activateCode;
	}
	

}
