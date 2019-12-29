package com.mainmicroservice.mainmicroservice.Controllers;

import java.util.UUID;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Services.MailService;
import com.mainmicroservice.mainmicroservice.Services.UsersService;


@RestController
public class UserController {

	@Autowired
	private UsersService us;
	@Autowired
	private MailService ms;
	@GetMapping("/user/{id}")
	public User getUser(@PathVariable Long id)
	{
		System.out.println("GetUser");
		return us.getUserById(id);
	}
	
	/*  Training
	@GetMapping("/email")
	public String email()
	{
		
		ms.SendMessage("Registration","helloArmen", "armentovmasyan02@gmail.com");
		System.out.println("Email");
		return "Spring Email";
	}
	*/
	
	@GetMapping("/activate/{code}")
	public void activate(@PathVariable String code)
	{
		User user=us.getUserByActivateCode(code);
	
		user.setIsActivate(true);
	  us.saveChanges(user);
		
	}
	
	@PostMapping("/registration")
	@CrossOrigin(origins="http://localhost:4200")
	public User angular( @RequestBody User us)
	{
		us.setIsActivate(false);
		us.setActivateCode(UUID.randomUUID().toString());
		this.us.addNewUser(us);
		ms.SendMessage("Registration", "Код для активации - http://localhost:4200/activate/"+us.getActivateCode(), us.getEmail());
		return us;
	}
	
}
