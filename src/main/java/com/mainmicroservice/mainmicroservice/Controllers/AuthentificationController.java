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
import javax.validation.Valid;

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
import com.mainmicroservice.mainmicroservice.Entities.UserDocument;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.MailService;
import com.mainmicroservice.mainmicroservice.Services.UserDocumentService;
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

	@Autowired
	private UserDocumentService udService;
	
	@PostMapping("user/updateUserImage")
	public ResponseEntity<?> updateUserImage(@RequestBody UploadFileModel fileModel, ServletRequest req)
	{
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    user.setUserImageKey("userImageKey_"+user.getUserid());
	    us.saveChanges(user);
	    fileModel.key=user.getUserImageKey();
	    
	    RestTemplate restTemplate = new RestTemplate();
		HttpEntity<UploadFileModel> entity = new HttpEntity<>(fileModel);
		return restTemplate.exchange("http://localhost:1234/uploadUserfile", HttpMethod.POST,entity, Object.class);
	}
	@GetMapping("/getRole")
	public ResponseEntity<UserInfoModel> getRole(ServletRequest req)
	{	
		try
		{
			String roleName=this.jwtTokenProvider.getRole((HttpServletRequest) req);
			UserInfoModel userInfo=new UserInfoModel();
			userInfo.roleName=roleName;
			return new ResponseEntity<>(userInfo,HttpStatus.OK);
		}
		catch(Exception ex)
		{
			return new ResponseEntity<>(null,HttpStatus.OK);
		}
	}
	
	@GetMapping("/getMyId")
	public ResponseEntity<Long> getMyId(ServletRequest req)
	{
		User user;
		try {
			String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
			user=us.findByEmail(userName);
		}
		catch(Exception ex)
		{
			 return new ResponseEntity<>(null,HttpStatus.OK); 
		}
		
	    return new ResponseEntity<>(user.getUserid(),HttpStatus.OK);
	}
	
	@GetMapping("/isLogin")
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
	    res.userId=user.getUserid();
	    user.setLastTimeWasONLINE(LocalDateTime.now());
	    us.saveChanges(user);
	    return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@GetMapping("/isOnline")
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
    
	   
    @PostMapping("signIn")
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
	public ResponseEntity<?> registation( @RequestBody @Valid RegistrationModel regModel)
	{
    	
		User user=new User();
		user.setIsActivate(false);
		user.setIsDeleted(false);
		user.setFirstname(regModel.firstname);
		user.setLastname(regModel.lastname);
		user.setEmail(regModel.email);
		user.setPassword(encoder.encode(regModel.password));
		user.setActivateCode(UUID.randomUUID().toString());
		user.setBirthDate(regModel.birthDate);
		user.setGender(regModel.gender);
		if(regModel.role.equals("TEACHER"))
		{
			user.setAboutMe(regModel.aboutMe);
			user.setEducationLevel(regModel.education);
			user.setReiting(4.0);
		}
		user.setRole(this.roleRepository.findByRoleName("ROLE_"+regModel.role));
		user = this.us.addNewUser(user);
		
		String[] keys=user.getDocumentKeys(regModel.allFiles);
		UploadFilesModel files=new UploadFilesModel(keys.length);
		for(int i=0;i<keys.length;i++)
		{
			UserDocument document=new UserDocument();
			document.setDocumentKey(keys[i]);
			document.setIsValid(true);
			document.setUser(user);
			udService.save(document);
			files.allFiles[i]=new UploadFileModel(keys[i],regModel.allFiles.get(i).content,regModel.allFiles.get(i).contentType);
		}
		
		
		if(user.getRole().getRoleName().equals("ROLE_TEACHER"))
		{
			RestTemplate restTemplate1 = new RestTemplate();
			HttpEntity<UploadFilesModel> entity1 = new HttpEntity<UploadFilesModel>(files);
			restTemplate1.exchange("http://localhost:1234/uploadCertificationFiles", HttpMethod.POST,entity1, Object.class);
		}
		
		
		UserModel usConversationModel = new UserModel(user);
		RestTemplate restTemplate2 = new RestTemplate();
		HttpEntity<UserModel> entity = new HttpEntity<UserModel>(usConversationModel);
		restTemplate2.exchange("http://localhost:8088/createUser", HttpMethod.POST,entity, Object.class );
		UserAndGroupUserModel userAndGroupUserModel = new UserAndGroupUserModel(user);
		HttpEntity<UserAndGroupUserModel> entityUG = new HttpEntity<UserAndGroupUserModel>(userAndGroupUserModel);
		restTemplate2.exchange("http://localhost:8090/createUser/", HttpMethod.POST,entityUG, Object.class );
		
		ms.SendMessage("Registration", "Код для активации - http://localhost:4200/activate/"+user.getActivateCode(), user.getEmail());
		
		return new ResponseEntity<>(us,HttpStatus.OK);
		
	}
	
}
