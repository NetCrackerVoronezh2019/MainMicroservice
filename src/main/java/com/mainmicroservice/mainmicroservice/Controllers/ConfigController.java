package com.mainmicroservice.mainmicroservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Services.RoleService;

import Models.AdvertisementModel;

import java.util.*;

@RestController
public class ConfigController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private Microservices microservices;
	
	
	
	@GetMapping("getAllRoles")
	private List<Role> getAllroles()
	{
		RestTemplate template=new RestTemplate();
		ResponseEntity<List<String>> res=template.exchange("http://192.168.99.101:7082/getAllRoles",HttpMethod.GET,null,new ParameterizedTypeReference<List<String>>(){});
		roleService.addNewRoles(res.getBody());
		return roleService.allRoles();
	}
	
	@GetMapping("getAllInfo")
	private Microservices getAllPorts()
	{
		return microservices;
	}
	
}
