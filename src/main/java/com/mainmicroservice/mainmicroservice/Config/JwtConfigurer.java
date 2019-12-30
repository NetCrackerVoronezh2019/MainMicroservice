package com.mainmicroservice.mainmicroservice.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mainmicroservice.mainmicroservice.Security.JwtTokenFilter;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private JwtTokenProvider jwtTokenProvider;

	
    public JwtConfigurer(JwtTokenProvider jwtTokenProvider) {
    	this.jwtTokenProvider=jwtTokenProvider;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //System.out.println("Config--"+SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString());
    }
}
