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
import Models.UserInfoModel;

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
	public boolean changeRole(@RequestBody String id)
	{
		try
		{
			User changedUser=us.getUserById(Long.getLong(id));
			Role role=roleRepository.findByRoleName("ROLE_ADMIN");
			changedUser.setRole(role);
			us.saveChanges(changedUser);
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
	}
}
