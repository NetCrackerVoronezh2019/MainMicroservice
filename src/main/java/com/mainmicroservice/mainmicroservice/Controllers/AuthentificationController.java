package com.mainmicroservice.mainmicroservice.Controllers;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.mainmicroservice.mainmicroservice.Entities.Role;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.MailService;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import Models.*;



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

	
	@GetMapping("gettoken")
	public String mytoken()
	{
		return jwtTokenProvider.createTokenForMicroservice();
	}
	
	@GetMapping("vtoken/{token}")
	public boolean validate(@PathVariable String token)
	{
		System.out.println(token);
		return jwtTokenProvider.validateMicroserviceToken(token);
	}
	@GetMapping("/getrole")
	public ResponseEntity<UserInfoModel> getRole(ServletRequest req)
	{	
		String roleName=this.jwtTokenProvider.getRole((HttpServletRequest) req);
		UserInfoModel userInfo=new UserInfoModel();
		userInfo.roleName=roleName;
		return new ResponseEntity<>(userInfo,HttpStatus.OK);
	}
	
	@GetMapping("/islogin")
	public ResponseEntity<UserInfoModel> isLogIn(ServletRequest req)
	{
		System.out.println("isLogin");
	    String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    if(userName==null)
	    {
	    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	    }
	    User user=us.findByEmail(userName);
	    UserInfoModel res=new UserInfoModel(userName,user.getRole().getRoleName());
	    return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@GetMapping("/isonline")
	public ResponseEntity<String> isOnline(ServletRequest req)
	{
		System.out.println("isLogin");
	    String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    if(userName==null)
	    {
	    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	    }
	    User user=us.findByEmail(userName);
	    user.setLastTimeWasONLINE(LocalDateTime.now());
	    us.saveChanges(user);
	    return new ResponseEntity<>(null,HttpStatus.OK);
	}
    
	@GetMapping("/activate/{code}")
	public ResponseEntity<?> activate(@PathVariable String code)
	{
		User user=us.getUserByActivateCode(code);
		user.setIsActivate(true);
		us.saveChanges(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
    
	   
    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody SignInModel signIn,ServletResponse response) {
    	
    	try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.email, signIn.password));
            User user = us.findByEmail(signIn.email);
            List<Role> roles=new ArrayList<Role>();
            roles.add(user.getRole());
            user.setLastTimeWasONLINE(LocalDateTime.now());
            us.saveChanges(user);
            String token = jwtTokenProvider.createToken(signIn.email, roles);
            AuthModel am=new AuthModel(signIn.email,token);
            return new ResponseEntity<>(am,HttpStatus.OK);
    	}
    	catch(AuthenticationException ex)
    	{
      		HttpHeaders httpHeaders = new HttpHeaders();
      	    httpHeaders.add("ErrorMessage", "error");
    		return new ResponseEntity<>(httpHeaders,HttpStatus.FORBIDDEN);
    	}
    }
	
	@PostMapping("/registration")
	public ResponseEntity<User> registation( @RequestBody RegistrationModel regModel)
	{
		User us=new User();
		us.setIsActivate(false);
		us.setIsDeleted(false);
		us.setFirstname(regModel.firstname);
		us.setLastname(regModel.lastname);
		us.setEmail(regModel.email);
		us.setPassword(encoder.encode(regModel.password));
		us.setActivateCode(UUID.randomUUID().toString());
		us.setRole(this.roleRepository.findByRoleName("ROLE_"+regModel.role));
		us = this.us.addNewUser(us);
		UploadFileModel file=new UploadFileModel();
		file.content=regModel.content;
		file.key="user_"+us.getUserid();
		UserModel usConversationModel = new UserModel(us);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<UserModel> entity = new HttpEntity<UserModel>(usConversationModel);
		restTemplate.exchange("http://localhost:8088/createUser", HttpMethod.POST,entity, Object.class );
		HttpEntity<UploadFileModel> requestEntity =new HttpEntity<>(file);
		restTemplate.exchange("http://localhost:1234/uploaduserfile",HttpMethod.POST,requestEntity,String.class);
		ms.SendMessage("Registration", "Код для активации - http://localhost:4200/activate/"+us.getActivateCode(), us.getEmail());
		return new ResponseEntity<>(us,HttpStatus.OK);
	}
	
}
