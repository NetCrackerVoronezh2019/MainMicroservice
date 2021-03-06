package com.mainmicroservice.mainmicroservice.Services;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRep;
	
	
	public void addNewRoles(List<String> strRoles)
	{
		List<Role> roles=roleRep.findAll();
		
		for(String role:strRoles)
		{
			if(roles.stream().noneMatch(rolex->rolex.getRoleName().equals(role)))
			{
				System.out.println("Role----"+role);
				Role newRole=new Role();
				newRole.setRoleName(role);
				roleRep.save(newRole);
			}
		}
		
		
	}
	
	public List<Role> allRoles()
	{
		return roleRep.findAll();
	}
	
	public Role findRoleByRoleName(String roleName)
	{
		return this.roleRep.findByRoleName(roleName);
	}
}
