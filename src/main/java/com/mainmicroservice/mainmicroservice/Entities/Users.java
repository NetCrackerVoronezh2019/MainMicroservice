package com.mainmicroservice.mainmicroservice.Entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private int age;
	
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
