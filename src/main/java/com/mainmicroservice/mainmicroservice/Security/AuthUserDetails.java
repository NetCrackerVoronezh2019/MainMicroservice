package com.mainmicroservice.mainmicroservice.Security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.mainmicroservice.mainmicroservice.Entities.User;
import java.util.*;

public class AuthUserDetails implements UserDetails {

	private User user;
	
	public AuthUserDetails(User user)
	{
		this.user=user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roleName=this.user.getRole().getRoleName();
		List<GrantedAuthority> authority=new ArrayList<GrantedAuthority>();
		authority.add(new SimpleGrantedAuthority(roleName));
		return authority;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.user.IsActivate;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.user.getIsDeleted();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
