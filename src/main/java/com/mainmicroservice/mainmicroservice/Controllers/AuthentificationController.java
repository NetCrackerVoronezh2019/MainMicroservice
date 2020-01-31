package com.mainmicroservice.mainmicroservice.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.MailService;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.AuthModel;
import Models.RegistrationModel;
import Models.SignInModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AuthentificationController {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired 
	private PasswordEncoder encoder;
	
    @Autowired
    private UserService us;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MailService ms;
	
	@Autowired
	private  AuthenticationManager authenticationManager;

	
	@GetMapping("/getrole")
	public ResponseEntity<AuthModel> getRole(ServletRequest req)
	{	
		String r=this.jwtTokenProvider.getRole((HttpServletRequest) req);
		AuthModel m = new AuthModel();
		m.email="gmail.com";
		m.token=r;
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
	
    
	@GetMapping("/activate/{code}")
	public void activate(@PathVariable String code)
	{
    	System.out.println(code);
		User user=us.getUserByActivateCode(code);
		user.setIsActivate(true);
		us.saveChanges(user);
		
	}
    
	   
   
    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody SignInModel signIn) {
        try {
            String username =signIn.email;
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.email, signIn.password));
            User user = us.findByEmail(username);
            List<Role> roles=new ArrayList<Role>();
            roles.add(user.getRole());
            user.setLastLogin(LocalDateTime.now());
            us.saveChanges(user);
            String token = jwtTokenProvider.createToken(username, roles);
            AuthModel am=new AuthModel();
            am.email=username;
            am.token=token;
            return new ResponseEntity<>(am,HttpStatus.OK);
        } catch (AuthenticationException e) {
        	
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
            
        }
    }
	
	@PostMapping("/registration")
	public ResponseEntity<User> registation( @RequestBody RegistrationModel regModel)
	{
		User us=new User();
		us.setIsActivate(false);
		us.setFirstname(regModel.firstname);
		us.setLastname(regModel.lastname);
		us.setEmail(regModel.email);
		us.setPassword(encoder.encode(regModel.password));
		us.setActivateCode(UUID.randomUUID().toString());
		us.setRole(this.roleRepository.findByRoleName("ROLE_"+regModel.role));
		this.us.addNewUser(us);
		ms.SendMessage("Registration", "Код для активации - http://localhost:4200/activate/"+us.getActivateCode(), us.getEmail());
		return new ResponseEntity<>(us,HttpStatus.OK);
	}
	
}
