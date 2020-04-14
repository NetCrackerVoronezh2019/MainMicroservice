package com.mainmicroservice.mainmicroservice.Controllers;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mainmicroservice.mainmicroservice.Entities.Rating;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.RatingService;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.ChangeOrderStatus;
import Models.ChangeReiting;
import Models.UserOrdersModel;
import Models.Enums.OrderStatus;
import Models.OrderModel;
import Models.RatingModel;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class OrderController {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserService us;
	
	@Autowired 
	private RatingService ratingService;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	@PostMapping("user/getAccessibleStatuses")
	public ResponseEntity<List<OrderStatus>> getAccessibleStatuses(@RequestBody  @NotNull OrderModel model)
	{
		HttpEntity<OrderModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderStatus>> res=restTemplate.exchange("http://localhost:1122/getAccessibleStatuses",HttpMethod.POST,entity,new ParameterizedTypeReference<List<OrderStatus>>(){});
		return res;
	}
	
	@GetMapping("user/getMyOrders")
	public ResponseEntity<?> getMyOrders(ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    String roleName=user.getRole().getRoleName();
	    Long id=user.getUserid();
		UserOrdersModel myOrder=new UserOrdersModel();
		myOrder.setRoleName(roleName);
		myOrder.setId(id);
		HttpEntity<UserOrdersModel> entity=new HttpEntity<>(myOrder);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderModel>> res=restTemplate.exchange("http://localhost:1122/getMyOrders",HttpMethod.POST,entity,new ParameterizedTypeReference<List<OrderModel>>(){});
		return res;
	}
	
	@PostMapping("user/changeOrderStatus")
	public ResponseEntity<?> getMyOrder(@RequestBody ChangeOrderStatus model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    String roleName=user.getRole().getRoleName();
	    Long id=user.getUserid();
	    model.setUserId(id);
	    model.setRoleName(roleName);
	    
	    
	    HttpEntity<ChangeOrderStatus> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> res=restTemplate.exchange("http://localhost:1122/changeOrderStatus",HttpMethod.POST,entity,Object.class);
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+model.getCustomerId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getCustomerId(),res2.getBody());
		return res;
	        
	}
	
	@PostMapping("user/changeReiting")
	public ResponseEntity<?> changeReiting(@RequestBody RatingModel model,ServletRequest req)
	{
		HttpEntity<RatingModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Object> res=restTemplate.exchange("http://localhost:1122/changeRating",HttpMethod.POST,entity,Object.class);
		ResponseEntity<Double> res1=restTemplate.exchange("http://localhost:1122/rating/"+model.getNotif().getSenderId(),HttpMethod.GET,null,Double.class);
		User user=us.getUserById(model.getNotif().getSenderId());
		if(res1.getBody()!=null)
		{
			user.setReiting(res1.getBody());
			us.saveChanges(user);
		}
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+model.getNotif().getSenderId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+model.getNotif().getSenderId(),res2.getBody());
		return res;
	
	}
	
	
}
