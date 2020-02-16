package com.mainmicroservice.mainmicroservice.Entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import Jacson.Views;

@Entity
@Table(name="ROLES")
public class Role {
	
	@Id
	@Column(name="ROLEID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long roleId;
	@Column(name="ROLENAME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String roleName;
	
	@JsonIgnore
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
