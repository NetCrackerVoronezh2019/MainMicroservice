package com.mainmicroservice.mainmicroservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;
import com.mainmicroservice.mainmicroservice.ElasticRepositorys.UserElasticSearchRepository;
import com.mainmicroservice.mainmicroservice.Entities.*;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Repositories.RoleRepository;
import com.mainmicroservice.mainmicroservice.Repositories.UserRepository;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.RoleService;
import com.mainmicroservice.mainmicroservice.Services.UserDocumentService;
import com.mainmicroservice.mainmicroservice.Services.UserElasticSearchService;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Jacson.Views;
import Models.AdvertisementModel;
import Models.CertificationNotModel;
import Models.ChangeDocumentValid;
import Models.ChangeUserDocument;
import Models.UserPageModel;
import Models.UserProp;
import Models.Enums.AdvertisementNotificationType;
import Models.Enums.TeacherStatus;

@RestController
@CrossOrigin(origins="https://helpui.herokuapp.com")
public class AdminController {

	@Autowired
    private UserService userService;

	@Autowired
	private UserElasticSearchService userESService;
	
	@Autowired
	private UserElasticSearchRepository userESRep;
	
	@Autowired
    private UserRepository userRep;;

	@Autowired 
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private Microservices microservices;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	@Autowired 
	private UserDocumentService udService;
	
	
	@GetMapping("getAllValidDocuments")
	public ResponseEntity<List<UserDocument>> getAllValidDocuments()
	{
		List<UserDocument> documents=this.udService.findAllValidDocuments();
		return new ResponseEntity<>(documents,HttpStatus.OK);
	}
	
	
	@GetMapping("getAllValidDocuments/{userId}")
	public ResponseEntity<List<UserDocument>> getUserValidDocuments(@PathVariable Long userId)
	{
		
		User us=this.userService.getUserById(userId);
		if(us!=null)
		{
			List<UserDocument> documents=us.getDocuments();
			if(documents!=null)
				documents=documents.stream().filter(d->d.getIsValid()==Boolean.TRUE)
									.collect(Collectors.toList());
			return new ResponseEntity<>(documents,HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null,HttpStatus.OK);
	
	}
	
	@GetMapping("getAllValidSubjects/{userId}")
	private ResponseEntity<Set<String>> getAllValidSubjects(@PathVariable Long userId)
	{
		User user=this.userService.getUserById(userId);
		Set<String> subjects=new HashSet<>();
		List<UserDocument> documents=user.getDocuments();
		documents=documents.stream()
				 .filter(doc->doc.getIsValid()==Boolean.TRUE)
				 .collect(Collectors.toList());
		for(UserDocument doc:documents)
			subjects.add(doc.getSubject());
		return new ResponseEntity<>(subjects,HttpStatus.OK);
			
	}
	
	@GetMapping("admin/getTeachersByStatus/{text}/{status}")
	public ResponseEntity<List<User>> getTeachersByStatus(@PathVariable String text, @PathVariable TeacherStatus status)
	{
		List<User> users;
		if(status==TeacherStatus.CERTIFICATES_ARE_NOT_CHECKED)
			users=this.userESService.filterNonCheckedTeacher(text);
		else
		{
			users=this.userESService.filterCheckedTeacher(text);
		}
		
		return new ResponseEntity<>(users,HttpStatus.OK);
	}
	
	@GetMapping("findES")
	public List<User> findAllUsersfromES()
	{
		List<User> users=new ArrayList<User>();
		for(User us:this.userESRep.findAll()) {
			users.add(us);
		}
		
		return users;
	}
	
	@GetMapping("delete")
	public void deleteEs()
	{
		this.userESRep.deleteAll();
	}
	
	@GetMapping("getAllUnValidDocuments")
	public ResponseEntity<List<UserDocument>> getAllUnValidDocuments()
	{
		List<UserDocument> documents=this.udService.findAllUnValidDocuments();
		return new ResponseEntity<>(documents,HttpStatus.OK);
		
	}
	
	@GetMapping("getAllTeachers")
	public List<User> getAllDocuments()
	{
		return this.userRep.findByRoleId()
				.stream()
				.filter(t->t.getTeacherStatus()!=TeacherStatus.CERTIFICATES_ARE_NOT_CHECKED)
				.collect(Collectors.toList());
	}
	
	@GetMapping("getAllUnCheckedTeachers")
	public List<User> getAllUncheckedTeachers()
	{
		return this.userRep.findByRoleId()
					.stream()
					.filter(t->t.getTeacherStatus()==TeacherStatus.CERTIFICATES_ARE_NOT_CHECKED)
					.collect(Collectors.toList());
	}
	
	@PostMapping("saveUserChanges")
	public ResponseEntity<?> saveChanges(@RequestBody ChangeUserDocument doc)
	{
		
		User newUser=userService.getUserById(doc.getUserId());
		UserDocument userDoc=doc.getUserDocument();
		userDoc.setUser(newUser);
		this.udService.save(userDoc);
		
		List<CertificationNotModel> certList=new ArrayList<>();
			CertificationNotModel not=new CertificationNotModel();
			not.setAddresseeId(newUser.getUserId());
			not.setCertificateName(userDoc.getDocumentKey());
			if(userDoc.getIsValid())
				not.setType(AdvertisementNotificationType.ACCEPTED_CERTIFICATION);
			if(!userDoc.getIsValid())
				not.setType(AdvertisementNotificationType.REJECTED_CERTIFICATION);
			certList.add(not);
			
		newUser=this.userService.setTeacherStatus(newUser);
		this.userService.saveChanges(newUser);
		try {
		this.userESRep.save(newUser);
		}
		catch(Exception ex){}
		
		RestTemplate restTemplate=new RestTemplate();
	    HttpEntity<List<CertificationNotModel>> entity=new HttpEntity<>(certList);
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Object> res= restTemplate.exchange("http://"+host+":"+advPort+"/certificationNotification",HttpMethod.POST,entity,Object.class);
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+newUser.getUserid(),HttpMethod.GET,null,new ParameterizedTypeReference<Integer>(){});
		
	    template.convertAndSend("/notification/"+newUser.getUserid(),res2.getBody());
	    
	    return new ResponseEntity<>(null,HttpStatus.OK);
	}
	
	
	@PostMapping("changeDocumentValidation")
	public ResponseEntity<?> changeDocumentValitation(@RequestBody @Valid ChangeDocumentValid model)
	{
		
		UserDocument ud=this.udService.findById(model.getId());
		if(ud!=null)
		{
			ud.setIsValid(model.getValidation());
			this.udService.save(ud);
		}
		
		return new ResponseEntity<>(null,HttpStatus.OK);

	}
	
	
	
	// менять 
	@PostMapping("admin/setRoles")
	public void setRoles(@RequestBody Role _role)
	{    
		 List<String> _roles=new ArrayList<String>();
		 _roles.add(_role.getRoleName());
		 RestTemplate restTemplate=new RestTemplate();
	     HttpEntity<List<String>> entity=new HttpEntity<List<String>>(_roles);
		 ResponseEntity<List<String>> res= restTemplate.exchange("http://localhost:"+"7082"+"/setRoles",HttpMethod.POST,entity,new ParameterizedTypeReference<List<String>>(){});
	}
	
	
	
	@GetMapping("admin/getAllRoles")
	public List<Role> getallroles()
	{
		return roleService.allRoles();
	}
	
	
	@PostMapping("admin/changeUser")
	public ResponseEntity<?> changeRole(@RequestBody UserProp model)
	{
			User changedUser=userService.getUserById(model.userId);
			changedUser.setFirstname(model.firstname);
			changedUser.setLastname(model.lastname);
			changedUser.setIsActivate(model.isActivate);
			changedUser.setEmail(model.email);
			changedUser.setIsDeleted(model.isDeleted);
			Role role=roleRepository.findByRoleName(model.role);
			changedUser.setRole(role);
			userService.saveChanges(changedUser);
			this.userESRep.save(changedUser);
			return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("admin/getAllUsers")
	@JsonView(Views.UserInfoForChangeProps.class)
	public ResponseEntity<List<User>> getAllUsers()
	{
		List<User> list=this.userService.getAllUsers();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	
	@GetMapping("getAllUsers")
	public ResponseEntity<List<User>> allUsers()
	{
		List<User> list=this.userService.getAllUsers();
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping("getUser/{userId}")
	public ResponseEntity<UserPageModel> getUser(@PathVariable Long userId,ServletRequest req)
	{
		User user=userService.getUserById(userId);
		UserPageModel m=UserPageModel.UserToModel(user);
	    return new ResponseEntity<>(m,HttpStatus.OK);
	}
	

}
