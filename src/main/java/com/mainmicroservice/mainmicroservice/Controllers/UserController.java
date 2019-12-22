package com.mainmicroservice.mainmicroservice.Controllers;

import javax.validation.Valid;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mainmicroservice.mainmicroservice.Entities.Users;
import com.mainmicroservice.mainmicroservice.Services.UsersService;


@RestController
public class UserController {

	@Autowired
	private UsersService us;
	
	@GetMapping("/user/{id}")
	public Users getUser(@PathVariable Long id)
	{
		System.out.println("GetUser");
		return us.getUserById(id);
	}
	
	@PostMapping("/adduser")
	public void addUser( @Valid @RequestBody Users us)
	{
		//System.out.println(us.getAge());
		this.us.addNewUser(us);
	}
	
	@PostMapping("/registration")
	@CrossOrigin(origins="http://localhost:4200")
	public Users angular( @RequestBody Users us)
	{
		System.out.println("Angular POST !");
		this.us.addNewUser(us);
		return us;
	}
	
	
	
}
