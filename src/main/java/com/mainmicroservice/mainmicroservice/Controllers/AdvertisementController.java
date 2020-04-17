package com.mainmicroservice.mainmicroservice.Controllers;
import java.io.IOException;
import java.time.LocalDateTime;
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
import Models.*;


@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdvertisementController {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private Microservices microInfo;
	
	
	@Autowired
	private UserService us;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	@GetMapping("user/setNotificatiosAsReaded")
	public ResponseEntity<?> setNotificationsAsReaded(ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long userId=user.getUserid();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> res=restTemplate.exchange("http://localhost:1122/setNotificatiosAsReaded/"+userId,HttpMethod.GET,null,Object.class);
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+userId,HttpMethod.GET,null,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+userId,res2.getBody());
		return res;
	}
	

	
	@GetMapping("canSendRequest/{advertisementId}")
	public ResponseEntity<Boolean> canSendRequest(@PathVariable Long advertisementId,ServletRequest req)
	{
		String roleName;
		Long id;
		try
		{
			String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
		    User user=us.findByEmail(userName);
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
	    HttpEntity<SendAdvertisementNotificationModel> entity=new HttpEntity<>(sendModel);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Boolean> res=restTemplate.exchange("http://localhost:1122/canSendRequest",HttpMethod.POST,entity,new ParameterizedTypeReference<Boolean>(){});
		return res;
	}
	
	@GetMapping("user/getMyAllNotifications") 
	public ResponseEntity<List<NotificationModel>> getAllNotifications(ServletRequest req)
	{

		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    Long id=user.getUserid();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<NotificationModel>> res=restTemplate.exchange("http://localhost:1122/getMyAllNotifications/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<List<NotificationModel>>(){});
		List<NotificationModel> not=res.getBody();
		for(NotificationModel model:not)
		{	
			Long id1=model.getAddresseeId();
			Long id2=model.getSenderId();
			model.setAddresseeUsername(this.us.getUserById(id1).getEmail());
			model.setSenderUsername(this.us.getUserById(id2).getEmail());		
			model.generateMessage();
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
	    HttpEntity<NotificationModel> entity=new HttpEntity<>(model);
		ResponseEntity<Object> res=restTemplate.exchange("http://localhost:1122/newNotification",HttpMethod.POST,entity,new ParameterizedTypeReference<Object>(){});
		//getMyAllNotificationsSize
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+model.getAddresseeId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getAddresseeId(),res2.getBody());
		return new ResponseEntity<>(null,res2.getStatusCode());
	}
	
	@PostMapping("user/notificationResponse")
	public ResponseEntity<?> responseNotification(@RequestBody NotificationModel model,ServletRequest req)
	{

	    RestTemplate restTemplate = new RestTemplate();
	    HttpEntity<NotificationModel> entity=new HttpEntity<>(model);
		ResponseEntity<Object> res=restTemplate.exchange("http://localhost:1122/notificationResponse",HttpMethod.POST,entity,new ParameterizedTypeReference<Object>(){});
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+model.getSenderId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
		ResponseEntity<Integer> res3=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+model.getAddresseeId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getSenderId(),res2.getBody());
	    template.convertAndSend("/notification/"+model.getAddresseeId(),res3.getBody());
		return new ResponseEntity<>(null,res2.getStatusCode());
	}
	
	@GetMapping("user/getMyAllNotificationsSize")
	public ResponseEntity<String> notSize(ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	    Long id=user.getUserid();
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Integer> res=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<Integer>(){});
		return new ResponseEntity<>(res.getBody().toString(),res.getStatusCode());
	}
	
	
	@PostMapping("isMyAdvertisement")
	public ResponseEntity<Boolean> isMyAdvertisement(@RequestBody IsUserAdvertisementModel model, ServletRequest req)
	{
		
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	   
	    model.userId=user.getUserid();
	    RestTemplate restTemplate = new RestTemplate();
		HttpEntity<IsUserAdvertisementModel> entity=new HttpEntity<>(model);
		ResponseEntity<Boolean> res=restTemplate.exchange("http://localhost:1122/isUserAdvertisement",HttpMethod.POST,entity,new ParameterizedTypeReference<Boolean>(){});
		return res;
	}
	@PostMapping("filterAdvertisements")
	public ResponseEntity<List<AdvertisementModel>> filterAdvertisements(@RequestBody AdvFilters advFilters)
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AdvFilters> entity=new HttpEntity<>(advFilters);
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://localhost:1122/filterAdvertisements",HttpMethod.POST,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
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
		HttpEntity<UserAdvertisementsModel> entity=new HttpEntity<>(userAdv);
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://localhost:1122/getStudentAdvertisements",HttpMethod.POST,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
		return res;
	}
	@GetMapping("getAllSubjects")
	public ResponseEntity<List<SubjectModel>> getAllSubjects()
	{
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<SubjectModel>> res=restTemplate.exchange("http://localhost:1122/allSubjects",HttpMethod.GET,null,new ParameterizedTypeReference<List<SubjectModel>>(){});
		return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@GetMapping("getAllFilters")
	public ResponseEntity<AdvFilters> getAllFilters()
	{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<SubjectModel>> res=restTemplate.exchange("http://localhost:1122/allSubjects",HttpMethod.GET,null,new ParameterizedTypeReference<List<SubjectModel>>(){});
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
	    HttpEntity<AdvertisementModel> requestEntity =new HttpEntity<>(advertisementModel);
		RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<Boolean> res=restTemplate.exchange("http://localhost:1122/addAdvertisement",HttpMethod.POST,requestEntity, Boolean.class );
		return res;
		
	}

	@GetMapping("allAdvertisements")
	public ResponseEntity<List<AdvertisementModel>> getAll() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", microInfo.getAdvertisement_token());
		HttpEntity<Object> entity = new HttpEntity<>(null, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://localhost:"+"1122"+"allAdvertisements",HttpMethod.GET,entity,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
	    return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@GetMapping("advertisement/{id}")
	public ResponseEntity<AdvertisementModel> getAdvertisementById(@PathVariable String id)
	{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AdvertisementModel> res=restTemplate.exchange("http://localhost:1122/advertisement/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<AdvertisementModel>(){});
		AdvertisementModel m=res.getBody();
		m.setFirstName(us.getUserById(m.getAuthorId()).getFirstname());
		m.setSurName(us.getUserById(m.getAuthorId()).getLastname());
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
	
	@PostMapping("admin/addNewSubject")
	public SubjectModel addNewSubject(@RequestBody SubjectModel _model)
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<SubjectModel> entity = new HttpEntity<SubjectModel>(_model);
		ResponseEntity<SubjectModel> res=restTemplate.exchange("http://localhost:7082/setNewSubject",HttpMethod.POST,entity, SubjectModel.class );
		return res.getBody();
	}
	
	
}
