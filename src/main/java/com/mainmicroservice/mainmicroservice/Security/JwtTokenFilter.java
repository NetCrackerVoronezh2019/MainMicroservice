package com.mainmicroservice.mainmicroservice.Security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtTokenFilter extends GenericFilterBean {
	
	 @Autowired
	 private JwtTokenProvider jwtTokenProvider;
	 
	   
	    /**
	     *
	     */
	    @Override
	    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
	            throws IOException, ServletException {
	        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
	        if (token != null && jwtTokenProvider.validateToken(token)) {
	            Authentication auth = jwtTokenProvider.getAuthentication(token);
	            if (auth != null) {
	                SecurityContextHolder.getContext().setAuthentication(auth);
	               
	            }
	        }
	        else
	        {
	        	System.out.println("Я тут");
	        	token=jwtTokenProvider.resolveMicroToken((HttpServletRequest) req);
	        	System.out.println("Прошли Resolve");
	        	if(token!=null && jwtTokenProvider.validateMicroserviceToken(token))
	        	{
	        		System.out.println("Прошли null и validate");
	        		Authentication auth=jwtTokenProvider.getAuthenticationForMicro(token);
	        		System.out.println("получили Authentication ");
	        		if (auth != null) {
	        			System.out.println("положили Authentication в SecurityContext");
		                SecurityContextHolder.getContext().setAuthentication(auth);
		               
		            }
	        		
	        	}
	        }
	        filterChain.doFilter(req, res);
	    }
}
