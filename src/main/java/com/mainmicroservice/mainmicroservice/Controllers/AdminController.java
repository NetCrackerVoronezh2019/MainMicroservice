package com.mainmicroservice.mainmicroservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.mainmicroservice.mainmicroservice.Entities.*;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.RoleService;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Jacson.Views;
import Models.AdvertisementModel;
import Models.UserPageModel;
import Models.UserProp;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdminController {

	@Autowired
    private UserService userService;

	@Autowired 
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@GetMapping("user/getAllUsers")
	@JsonView(Views.UserInfoForChangeProps.class)
	public ResponseEntity<List<User>> getAllUsers()
	{
		List<User> list=this.userService.getAllUsers();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("getUser/{userId}")
	public ResponseEntity<UserPageModel> getUser(@PathVariable Long userId,ServletRequest req)
	{
		User user=userService.getUserById(userId);
		UserPageModel m=UserPageModel.UserToModel(user);
	    return new ResponseEntity<>(m,HttpStatus.OK);
	}
	
	
	
	@PostMapping("admin/setRoles")
	public void setRoles(@RequestBody Role _role)
	{    
		 List<String> _roles=new ArrayList<String>();
		 _roles.add(_role.getRoleName());
		 RestTemplate restTemplate=new RestTemplate();
	     HttpEntity<List<String>> entity=new HttpEntity<List<String>>(_roles);
		 ResponseEntity<List<String>> res= restTemplate.exchange("http://localhost:"+"7082"+"/setroles",HttpMethod.POST,entity,new ParameterizedTypeReference<List<String>>(){});
	}
	
	
	
	@GetMapping("admin/getAllRoles")
	public List<Role> getallroles()
	{
		return roleService.allRoles();
	}
	
	
	@PostMapping("admin/changeUser")
	public ResponseEntity<?> changeRole(@RequestBody UserProp model)
	{
			User changedUser=userService.getUserById(model.userId);
			changedUser.setFirstname(model.firstname);
			changedUser.setLastname(model.lastname);
			changedUser.setIsActivate(model.isActivate);
			changedUser.setEmail(model.email);
			changedUser.setIsDeleted(model.isDeleted);
			Role role=roleRepository.findByRoleName(model.role);
			changedUser.setRole(role);
			userService.saveChanges(changedUser);
			return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
