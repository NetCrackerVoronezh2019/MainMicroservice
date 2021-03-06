package com.mainmicroservice.mainmicroservice.Controllers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import com.mainmicroservice.mainmicroservice.Sort.NotificationComporator;

import Models.*;
import Models.Enums.AdvertisementNotificationType;
import Models.Enums.AdvertisementStatus;
import Models.Enums.AdvertisementType;
import Models.Enums.BlockType;


@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdvertisementController {

	
	@Autowired
	private Microservices microservices;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private Microservices microInfo;
	
	
	@Autowired
	private UserService us;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	@GetMapping("deleteAdvertisement/{id}/{comment}")
	public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id,@PathVariable String comment)
	{
		 RestTemplate restTemplate = new RestTemplate();
		 HttpHeaders headers = new HttpHeaders();
		 headers.set("Authorization", microInfo.getAdvertisement_token());
		 HttpEntity<Object> entity = new HttpEntity<>(headers);
		 String host=microservices.getHost();
		 String advPort=microservices.getAdvertismentPort();
		 ResponseEntity<Long> res=restTemplate.exchange("http://"+host+":"+advPort+"/deleteAdvertisement/"+id+"/"+comment,HttpMethod.GET,entity,new ParameterizedTypeReference<Long>(){});
		 ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+res.getBody(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
		 template.convertAndSend("/notification/"+res.getBody(),res2.getBody());
		 return res;
	}
	@GetMapping("user/setNotificatiosAsReaded")
	public ResponseEntity<?> setNotificationsAsReaded(ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long userId=user.getUserid();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> res=restTemplate.exchange("http://"+host+":"+advPort+"/setNotificatiosAsReaded/"+userId,HttpMethod.GET,entity,Object.class);
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+userId,HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+userId,res2.getBody());
		return res;
	}
	

	
	@GetMapping("user/changeAdvStatus/{advId}/{status}")
	public ResponseEntity<AdvertisementModel> changeAdvertisementStatus(@PathVariable Long advId,@PathVariable AdvertisementStatus status )
	{
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<AdvertisementModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/changeAdvStatus/"+advId+"/"+status,HttpMethod.GET,entity,AdvertisementModel.class);	
		return res;
	}
	@GetMapping("canSendRequest/{advertisementId}")
	public ResponseEntity<Boolean> canSendRequest(@PathVariable Long advertisementId,ServletRequest req)
	{
		String roleName;
		Long id;
		User user;
		try
		{
			String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
		    user=us.findByEmail(userName);
		    roleName=user.getRole().getRoleName();
		    id=user.getUserid();
		}
		catch(Exception ex)
		{   
			return new ResponseEntity<>(false,HttpStatus.OK);
		}
		
	    SendAdvertisementNotificationModel sendModel=new SendAdvertisementNotificationModel();
	    sendModel.setRoleName(roleName);
	    sendModel.setAdvertisementId(advertisementId);
	    sendModel.setSenderId(id);
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
	
	    HttpEntity<SendAdvertisementNotificationModel> entity=new HttpEntity<>(sendModel,headers);
	    RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Boolean> res=restTemplate.exchange("http://"+host+":"+advPort+"/canSendRequest",HttpMethod.POST,entity,new ParameterizedTypeReference<Boolean>(){});
		BlockType type=user.getBlockType();
		if(type==BlockType.NONE && res.getBody())
			return new ResponseEntity<>(Boolean.TRUE,HttpStatus.OK);
		return new ResponseEntity<>(Boolean.FALSE,HttpStatus.OK);
	}
	
	@GetMapping("user/getMyAllNotifications") 
	public ResponseEntity<List<FullNotificationModel>> getAllNotifications(ServletRequest req)
	{

		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long id=user.getUserid();
		RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<List<FullNotificationModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotifications/"+id,HttpMethod.GET,entity,new ParameterizedTypeReference<List<FullNotificationModel>>(){});
		List<FullNotificationModel> not=res.getBody();
		for(FullNotificationModel fullModel:not)
		{	
			NotificationModel model=fullModel.getNotification();
			if(fullModel.getNotification().getType()!=AdvertisementNotificationType.ACCEPTED_CERTIFICATION
					&& fullModel.getNotification().getType()!=AdvertisementNotificationType.REJECTED_CERTIFICATION
					&& fullModel.getNotification().getType()!=AdvertisementNotificationType.DELETE_ADVERTISEMENT)
			{
			
			Long id1=model.getAddresseeId();
			Long id2=model.getSenderId();
			User sender=this.us.getUserById(id2);
			model.setAddresseeUsername(this.us.getUserById(id1).getEmail());
			model.setSenderUsername(sender.getEmail());	
			model.setSenderFIO(sender.getFirstname()+" "+sender.getLastname());
			model.setUserImageKey(sender.getUserImageKey());
			
			}
			
			//model.generateMessage();
			fullModel.setNotification(model);
		}	
		
		 
		
		not.sort(new NotificationComporator());
		return new ResponseEntity<>(not,HttpStatus.OK);	
	}
	
	
	@GetMapping("getCommonNots/{senderId}") 
	public ResponseEntity<List<FullNotificationModel>> getCommon(@PathVariable Long senderId,ServletRequest req)
	{

		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long id=user.getUserid();
	    
		RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<List<FullNotificationModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getCommonNots/"+senderId+"/"+id,HttpMethod.GET,entity,new ParameterizedTypeReference<List<FullNotificationModel>>(){});
		List<FullNotificationModel> not=res.getBody();
		for(FullNotificationModel model:not)
		{	
			Long id1=model.getNotification().getAddresseeId();
			Long id2=model.getNotification().getSenderId();
			User sender=this.us.getUserById(id2);
			model.getNotification().setAddresseeUsername(this.us.getUserById(id1).getEmail());
			model.getNotification().setSenderUsername(sender.getEmail());	
			model.getNotification().setSenderFIO(sender.getFirstname()+" "+sender.getLastname());
			model.getNotification().generateMessage();
		}	
		return new ResponseEntity<>(not,HttpStatus.OK);	
	}
	
	@PostMapping("user/newNotification")
	public ResponseEntity<?> newNotification(@RequestBody NotificationModel model,ServletRequest req)
	{
		// отправляем новое уведомление 
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	    model.setSenderId(user.getUserid());
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
	    HttpEntity<NotificationModel> entity=new HttpEntity<>(model,headers);
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Object> res=restTemplate.exchange("http://"+host+":"+advPort+"/newNotification",HttpMethod.POST,entity,new ParameterizedTypeReference<Object>(){});
		//getMyAllNotificationsSize
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+model.getAddresseeId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getAddresseeId(),res2.getBody());
		return new ResponseEntity<>(null,res2.getStatusCode());
	}
	
	@PostMapping("user/notificationResponse")
	public ResponseEntity<?> responseNotification(@RequestBody NotificationModel model,ServletRequest req)
	{

	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
	    HttpEntity<NotificationModel> entity=new HttpEntity<>(model,headers);
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Object> res=restTemplate.exchange("http://"+host+":"+advPort+"/notificationResponse",HttpMethod.POST,entity,new ParameterizedTypeReference<Object>(){});
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+model.getSenderId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
		ResponseEntity<Integer> res3=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+model.getAddresseeId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getSenderId(),res2.getBody());
	    template.convertAndSend("/notification/"+model.getAddresseeId(),res3.getBody());
		return new ResponseEntity<>(null,res2.getStatusCode());
	}
	
	@GetMapping("user/getMyAllNotificationsSize")
	public ResponseEntity<String> notSize(ServletRequest req)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<SubjectModel> entity = new HttpEntity<SubjectModel>(headers);
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	    Long id=user.getUserid();
	    RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Integer> res=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+id,HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
		return new ResponseEntity<>(res.getBody().toString(),res.getStatusCode());
	}
	
	
	@PostMapping("isMyAdvertisement")
	public ResponseEntity<Boolean> isMyAdvertisement(@RequestBody IsUserAdvertisementModel model, ServletRequest req)
	{
		
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	   
	    model.userId=user.getUserid();
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<IsUserAdvertisementModel> entity=new HttpEntity<>(model,headers);
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Boolean> res=restTemplate.exchange("http://"+host+":"+advPort+"/isUserAdvertisement",HttpMethod.POST,entity,new ParameterizedTypeReference<Boolean>(){});
		return res;
	}
	@PostMapping("filterAdvertisements")
	public ResponseEntity<List<AdvertisementModel>> filterAdvertisements(@RequestBody AdvFilters advFilters)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AdvFilters> entity=new HttpEntity<>(advFilters,headers);
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/filterAdvertisements",HttpMethod.POST,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
		System.out.println(res.getBody().size());
		return res;		
	}
	
	@PostMapping("user/getMyAdvertisements")
	public ResponseEntity<List<AdvertisementModel>> getStudentAdvertisements(@RequestBody UserAdvertisementsModel userAdv,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    userAdv.setUserId(user.getUserid());
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		
		HttpEntity<UserAdvertisementsModel> entity=new HttpEntity<>(userAdv,headers);
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getStudentAdvertisements",HttpMethod.POST,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
		return res;
	}
	@GetMapping("getAllSubjects")
	public ResponseEntity<List<SubjectModel>> getAllSubjects()
	{
	
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<SubjectModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/allSubjects",HttpMethod.GET,entity,new ParameterizedTypeReference<List<SubjectModel>>(){});
		return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@GetMapping("getAllFilters")
	public ResponseEntity<AdvFilters> getAllFilters()
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<SubjectModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/allSubjects",HttpMethod.GET,entity,new ParameterizedTypeReference<List<SubjectModel>>(){});
		AdvFilters filter=new AdvFilters();
		filter.setSubjects(res.getBody());
		return new ResponseEntity<>(filter,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="user/addAdvertisement",method = RequestMethod.POST)
	public ResponseEntity<Boolean> addAdvertisement(@RequestBody AdvertisementModel advertisementModel, ServletRequest req) throws IOException
	{
	    String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long id=user.getUserid();
	    advertisementModel.setAuthorRole(user.getRole().getRoleName());
		advertisementModel.setAuthorId(id);	
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
	    HttpEntity<AdvertisementModel> requestEntity =new HttpEntity<>(advertisementModel,headers);
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    ResponseEntity<Boolean> res=restTemplate.exchange("http://"+host+":"+advPort+"/addAdvertisement",HttpMethod.POST,requestEntity, Boolean.class );
		return res;
		
	}
	
	
	@PostMapping("updateAdvertisementInformation")
	public ResponseEntity<?> updateAdvertisement(@RequestBody AdvertisementModel model){
		
		 HttpHeaders headers = new HttpHeaders();
		 headers.set("Authorization", microInfo.getAdvertisement_token());
		 HttpEntity<AdvertisementModel> requestEntity =new HttpEntity<>(model,headers);
		 RestTemplate restTemplate = new RestTemplate();
		 String host=microservices.getHost();
		 String advPort=microservices.getAdvertismentPort();
		 ResponseEntity<Boolean> res=restTemplate.exchange("http://"+host+":"+advPort+"/updateAdvertisementInformation",HttpMethod.POST,requestEntity, Boolean.class );
	     return res;
	}

	@GetMapping("allAdvertisements")
	public ResponseEntity<List<AdvertisementModel>> getAll() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/allAdvertisements",HttpMethod.GET,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
	    return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@GetMapping("advertisement/{id}")
	public ResponseEntity<AdvertisementModel> getAdvertisementById(@PathVariable String id)
	{
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		ResponseEntity<AdvertisementModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/advertisement/"+id,HttpMethod.GET,entity,new ParameterizedTypeReference<AdvertisementModel>(){});
		AdvertisementModel m=res.getBody();
		m.setFirstName(us.getUserById(m.getAuthorId()).getFirstname());
		m.setSurName(us.getUserById(m.getAuthorId()).getLastname());
		
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
	
	@PostMapping("admin/addNewSubject")
	public ResponseEntity<?> addNewSubject(@RequestBody SubjectModel _model)
	{
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<SubjectModel> entity = new HttpEntity<SubjectModel>(_model,headers);
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    String ugPort=microservices.getUserAndgroupsPort();
	   try {
	    ResponseEntity<SubjectModel> res1=restTemplate.exchange("http://"+host+":"+advPort+"/addNewSubject",HttpMethod.POST,entity, SubjectModel.class);
	    ResponseEntity<SubjectModel> res2=restTemplate.exchange("http://"+host+":"+ugPort+"/addNewSubject",HttpMethod.POST,entity, SubjectModel.class);
	    }
	    catch(Exception ex) {}
	    
	
		return new ResponseEntity<>(null,HttpStatus.OK);
	}
	
	@PostMapping("editSubject")
	public ResponseEntity<?> editSubject(@RequestBody EditSubject model)
	{
		
		RestTemplate restTemplate = new RestTemplate();
	    String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    String ugPort=microservices.getUserAndgroupsPort();
	    HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
	    HttpEntity<EditSubject> entity = new HttpEntity<>(model,headers);
	   try {
	      ResponseEntity<Object> res1=restTemplate.exchange("http://"+host+":"+advPort+"/editSubject",HttpMethod.POST,entity, Object.class);
	 //  ResponseEntity<SubjectModel> res2=restTemplate.exchange("http://"+host+":"+ugPort+"/editSubject",HttpMethod.POST,entity, SubjectModel.class);
	    }
	    catch(Exception ex) {}
	    
		//ResponseEntity<SubjectModel> res=restTemplate.exchange("http://localhost:7082/setNewSubject",HttpMethod.POST,entity, SubjectModel.class );
		return new ResponseEntity<>(null,HttpStatus.OK);
		
	}
	
}
