package com.mainmicroservice.mainmicroservice.Security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.UserInfoModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;
    
    @Value("${jwt.token.microserviceSecret}")
    private String microserviceSecret;

    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    
    private UserService us;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    
    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }
    
    
    public String createTokenForMicroservice()
    {
    	Claims claims=Jwts.claims().setSubject("MainMicroservice");
    	 return Jwts.builder()
                 .setClaims(claims)
                 .signWith(SignatureAlgorithm.HS256, this.microserviceSecret)
                 .compact();
    }
    
    
    public boolean validateMicroserviceToken(String token)
    {
    	 try {
             Jws<Claims> claims = Jwts.parser().setSigningKey(this.microserviceSecret).parseClaimsJws(token);
             if (!claims.getBody().getSubject().equals("MainMicroservice")) {
                 return false;
             }

             return true;
         } catch (JwtException |IllegalArgumentException e) {
            return false;
         }
    }

    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        if(userDetails!=null)
        {
        	
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    
    public Authentication getAuthenticationForMicro(String token) {
        return new UsernamePasswordAuthenticationToken(new AuthMicroserviceDetails(), "", new AuthMicroserviceDetails().getAuthorities());
    }
    
   

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    
    
    public String resolveMicroToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null) {
            return token;
        }
        return null;
    }
    
    public String getUsername(HttpServletRequest req)
    {
    	String token = this.resolveToken((HttpServletRequest) req);
    	if(token==null)
    		return null;
    	else {
	    		if(this.validateToken(token))
	    		{
		    		String userName=this.getUsername(token);
		    		return userName;
	    		}
    	}
    	return null;
    	
    }
    
    public String getRole(HttpServletRequest req)
    {
        
    	String token = this.resolveToken((HttpServletRequest) req);
		Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        @SuppressWarnings("unchecked")
		List<String> roles=(List<String>)claims.getBody().get("roles");
        return roles.get(0);
    }
    
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException |IllegalArgumentException e) {
           return false;
        }
    }

    
    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getRoleName());
        });
        
        System.out.println("ROLENAME-"+result.get(0));

        return result;
    }

}
