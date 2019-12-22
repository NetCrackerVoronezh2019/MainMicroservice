package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.*;
import javax.validation.constraints.*;



@Entity
@Table(name = "Users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Userid;
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
	

	
	

}
