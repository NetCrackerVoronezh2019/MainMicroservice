package com.mainmicroservice.mainmicroservice.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="ROLES")
public class Role {
	
	@Id
	@Column(name="ROLEID")
	private long roleId;
	@Column(name="ROLENAME")
	private String roleName;
	
	@OneToMany(cascade = CascadeType.ALL,
		    fetch = FetchType.EAGER,
		    mappedBy = "role")
	private Set<User> allusers=new HashSet<>();
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Set<User> getAllusers() {
		return allusers;
	}
	public void setAllusers(Set<User> allusers) {
		this.allusers = allusers;
	}
	
}
