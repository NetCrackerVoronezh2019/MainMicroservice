package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.*;
import javax.validation.constraints.*;



@Entity
@Table(name = "Users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long Userid;
	@Size(min=5, max=20)
	private String firstname;
	@Size(min=5, max=20)
	private String lastname;
	@Email
	private String email;
	private String password;
	@Max(90)
	private int age;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICEID", nullable = false)
    private Services service;
	
	public String getFirstname()
	{
		return this.firstname;
	}
	public String getLastName()
	{
		return this.lastname;
	}
	public String getPassword()
	{
		return this.password;
	}
	public int getAge()
	{
		return this.age;
	}
	
	

}
