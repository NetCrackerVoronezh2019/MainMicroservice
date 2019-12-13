package com.mainmicroservice.mainmicroservice.Controllers;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Users getUser()
	{
		System.out.println("GetUser");
		return us.GetNameById();
	}
	
	@PostMapping("/adduser")
	public void addUser( @RequestBody Users us)
	{
		System.out.println("PostUser---"+ us.getFirstname());
	}
	
}
