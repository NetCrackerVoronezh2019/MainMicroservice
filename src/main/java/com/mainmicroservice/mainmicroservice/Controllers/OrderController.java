package com.mainmicroservice.mainmicroservice.Controllers;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import Models.IsMyOrder;
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
	
	
	@GetMapping("getFreelancerAllFeedBack/{id}")
	public ResponseEntity<List<OrderModel>> getFreelancerAllFeedBack(@PathVariable Long id,ServletRequest req)
	{
	
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderModel>> res=restTemplate.exchange("http://localhost:1122/getFreelancerAllFeedBack/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<List<OrderModel>>(){});
		return res;
	    
	}
	
	@PostMapping("user/isMyOrder")
	public ResponseEntity<OrderModel> isMyOrder(@RequestBody @Valid IsMyOrder model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    model.setUserId(user.getUserId());
	    HttpEntity<IsMyOrder> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrderModel> res=restTemplate.exchange("http://localhost:1122/isMyOrder",HttpMethod.POST,entity,new ParameterizedTypeReference<OrderModel>(){});
		return res;
	    
	}
	
	@PostMapping("user/getAccessibleStatuses")
	public ResponseEntity<OrderStatus> getAccessibleStatuses(@RequestBody  @NotNull OrderModel model)
	{
		HttpEntity<OrderModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrderStatus> res=restTemplate.exchange("http://localhost:1122/getAccessibleStatuses",HttpMethod.POST,entity,new ParameterizedTypeReference<OrderStatus>(){});
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
		List<OrderModel> orderModels=res.getBody();
		for(OrderModel om:orderModels)
		{
			if(roleName.equals("ROLE_TEACHER"))
			{	User userx=us.getUserById(om.getCustomerId());
				om.setCustomerFIO(userx.getFirstname()+" "+userx.getLastname());
			}
			else
			{
				User userx=us.getUserById(om.getFreelancerId());
				om.setFreelancerFIO(userx.getFirstname()+" "+userx.getLastname());
			}
		}
		return new ResponseEntity<>(orderModels,HttpStatus.OK);
	}
	
	@PostMapping("user/changeOrderStatus")
	public ResponseEntity<?> getMyOrder(@RequestBody ChangeOrderStatus model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    String roleName=user.getRole().getRoleName();
	    model.setRoleName(roleName);
	    model.setUserId(user.getUserId());
	    
	    HttpEntity<ChangeOrderStatus> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrderModel> res=restTemplate.exchange("http://localhost:1122/changeOrderStatus",HttpMethod.POST,entity,OrderModel.class);
		OrderModel orderModel=res.getBody();
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+orderModel.getCustomerId(),HttpMethod.GET,null,new ParameterizedTypeReference<Integer>(){});
		
	    template.convertAndSend("/notification/"+orderModel.getCustomerId(),res2.getBody());
		return res;
	        
	}
	
	@PostMapping("user/changeReiting")
	public ResponseEntity<?> changeReiting(@RequestBody RatingModel model,ServletRequest req)
	{
		HttpEntity<RatingModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<OrderModel> res=restTemplate.exchange("http://localhost:1122/changeRating",HttpMethod.POST,entity,OrderModel.class);
	    OrderModel orderModel=res.getBody();
		ResponseEntity<Double> res1=restTemplate.exchange("http://localhost:1122/rating/"+orderModel.getFreelancerId(),HttpMethod.GET,null,Double.class);
		User user=us.getUserById(orderModel.getFreelancerId());
		if(res1.getBody()!=null)
		{
			user.setReiting(res1.getBody());
			us.saveChanges(user);
		}
		ResponseEntity<Integer> res2=restTemplate.exchange("http://localhost:1122/getMyAllNotificationsSize/"+orderModel.getFreelancerId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+orderModel.getFreelancerId(),res2.getBody());
	    
		return res;
	
	}
	
	
}
