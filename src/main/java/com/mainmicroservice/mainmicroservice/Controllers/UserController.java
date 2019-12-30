package com.mainmicroservice.mainmicroservice.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.MailService;
import com.mainmicroservice.mainmicroservice.Services.UserService;


@RestController
public class UserController {



	@Autowired
	private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService us;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MailService ms;
	

	
	@GetMapping("/activate/{code}")
	public void activate(@PathVariable String code)
	{
		User user=us.getUserByActivateCode(code);
	
		user.setIsActivate(true);
	  us.saveChanges(user);
		
	}
	
    @SuppressWarnings("rawtypes")
    @GetMapping("login/{l}/{p}")
    public ResponseEntity login(@PathVariable String l, @PathVariable String p) {
        try {
            String username =l;
          
            User user = us.findByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }
            List<Role> roles=new ArrayList<Role>();
            roles.add(user.getRole());
            String token = jwtTokenProvider.createToken(username, roles);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
	
	@PostMapping("/registration")
	@CrossOrigin(origins="http://localhost:4200")
	public User registation( @RequestBody User us)
	{
		us.setIsActivate(false);
		us.setActivateCode(UUID.randomUUID().toString());
		us.setRole(this.roleRepository.findByRoleName("ROLE_USER"));
		this.us.addNewUser(us);
		ms.SendMessage("Registration", "Код для активации - http://localhost:8080/activate/"+us.getActivateCode(), us.getEmail());
		return us;
	}
	
}
