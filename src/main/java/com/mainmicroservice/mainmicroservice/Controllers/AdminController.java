package com.mainmicroservice.mainmicroservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.mainmicroservice.mainmicroservice.Entities.*;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Jacson.Views;
import Models.UserProp;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdminController {

	@Autowired
    private UserService us;

	@Autowired
	private RoleRepository roleRepository;
	
	@GetMapping("admin/getallusers")
	@JsonView(Views.UserInfoForChangeProps.class)
	public ResponseEntity<List<User>> getAllUsers()
	{
		List<User> list=this.us.getAllUsers();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	
	@PostMapping("admin/changeuser")
	public boolean changeRole(@RequestBody UserProp model)
	{
			//User changedUser=us.getUserById(Long.parseLong(model.userId));
			User changedUser=us.getUserById(model.userId);
			changedUser.setFirstname(model.firstname);
			changedUser.setLastname(model.lastname);
			changedUser.setIsActivate(model.isActivate);
			changedUser.setEmail(model.email);
			changedUser.setIsDeleted(model.isDeleted);
			Role role=roleRepository.findByRoleName(model.role);
			changedUser.setRole(role);
			us.saveChanges(changedUser);
			System.out.println("end");
			return true;

	}
}
