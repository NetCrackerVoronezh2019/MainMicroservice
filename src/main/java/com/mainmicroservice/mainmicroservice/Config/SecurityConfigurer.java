package com.mainmicroservice.mainmicroservice.Config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mainmicroservice.mainmicroservice.Security.JwtTokenFilter;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;


@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private JwtTokenProvider jwtTokenProvider;
	@Autowired
	public JwtTokenFilter jwtFilter;
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		//JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
		
		http
		.cors()
		.and()
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()    
        .antMatchers("/user/**").hasRole("USER")
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
        .and()
		 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                
    }

}

