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
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.RatingService;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.ChangeOrderStatus;
import Models.ChangeReiting;
import Models.CompleteOrderModel;
import Models.DeleteOrderAttachments;
import Models.IsMyOrder;
import Models.MyOrderModel;
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
	private Microservices microservices;
	
	@Autowired 
	private RatingService ratingService;
	
	@Autowired
    private SimpMessagingTemplate template;
	
	
	
	@PostMapping("deleteOrderAttachments")
	public ResponseEntity<?> deleteOrderAttachments(@RequestBody DeleteOrderAttachments model)
	{
		RestTemplate restTemplate = new RestTemplate();
	    HttpEntity<DeleteOrderAttachments> entity=new HttpEntity<>(model);
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Object> res=restTemplate.exchange("http://"+host+":"+advPort+"/deleteOrderAttachments/",HttpMethod.POST,entity,Object.class);
		return res;
	}
	@PostMapping("user/addAttachments")
	public ResponseEntity<?> completeOrder(@RequestBody CompleteOrderModel model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);	
	    model.setUserId(user.getUserId());
	    model.setRoleName(user.getRole().getRoleName());
	    RestTemplate restTemplate = new RestTemplate();
	    HttpEntity<CompleteOrderModel> entity=new HttpEntity<>(model);
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<Object> res=restTemplate.exchange("http://"+host+":"+advPort+"/addAttachments/",HttpMethod.POST,entity,Object.class);
		return res;
	}
	
	@GetMapping("getFreelancerAllFeedBack/{id}")
	public ResponseEntity<List<OrderModel>> getFreelancerAllFeedBack(@PathVariable Long id,ServletRequest req)
	{
	
	    RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<OrderModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getFreelancerAllFeedBack/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<List<OrderModel>>(){});
		List<OrderModel> orders=res.getBody();
		for(OrderModel om:orders)
		{

				User userx=us.getUserById(om.getCustomerId());
				om.setCustomerFIO(userx.getFirstname()+" "+userx.getLastname());
				om.setCustomerImageKey(userx.getUserImageKey());

		}
		return new ResponseEntity<>(orders,HttpStatus.OK);
	    
	}
	
	@PostMapping("user/isMyOrder")
	public ResponseEntity<OrderModel> isMyOrder(@RequestBody @Valid IsMyOrder model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    model.setUserId(user.getUserId());
	    HttpEntity<IsMyOrder> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<OrderModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/isMyOrder",HttpMethod.POST,entity,new ParameterizedTypeReference<OrderModel>(){});
		return res;
	    
	}
	
	@PostMapping("user/getUserOrdersByOrderStatus")
	public ResponseEntity<List<OrderModel>> getUserOrdersByOrderStatus(@RequestBody MyOrderModel model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    String roleName=user.getRole().getRoleName();
	    model.setMyId(user.getUserId());
	    model.setRole(user.getRole().getRoleName());
	    HttpEntity<MyOrderModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<List<OrderModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getUserOrdersByOrderStatus",HttpMethod.POST,entity,new ParameterizedTypeReference<List<OrderModel>>(){});
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
	
	@PostMapping("user/getAccessibleStatuses")
	public ResponseEntity<OrderStatus> getAccessibleStatuses(@RequestBody  @NotNull OrderModel model)
	{
		HttpEntity<OrderModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<OrderStatus> res=restTemplate.exchange("http://"+host+":"+advPort+"/getAccessibleStatuses",HttpMethod.POST,entity,new ParameterizedTypeReference<OrderStatus>(){});
		return res;
	}
	
	@GetMapping("user/getOrder/{orderId}")
	public ResponseEntity<OrderModel> getOrder(@PathVariable Long orderId,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User user=us.findByEmail(userName);
	    String roleName=user.getRole().getRoleName();
		RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
		ResponseEntity<OrderModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/getOrder/"+orderId,HttpMethod.GET,null,OrderModel.class);
		OrderModel orderModel=res.getBody();
		 if(roleName.equals("ROLE_TEACHER"))
		 {	User userx=us.getUserById(orderModel.getCustomerId());
			orderModel.setCustomerFIO(userx.getFirstname()+" "+userx.getLastname());
		 }
		 else
		 {
			User userx=us.getUserById(orderModel.getFreelancerId());
			orderModel.setFreelancerFIO(userx.getFirstname()+" "+userx.getLastname());
		 }
		
		 return new ResponseEntity<>(orderModel,HttpStatus.OK);
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
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<OrderModel>> res=restTemplate.exchange("http://"+host+":"+advPort+"/getMyOrders",HttpMethod.POST,entity,new ParameterizedTypeReference<List<OrderModel>>(){});
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
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    HttpEntity<ChangeOrderStatus> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrderModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/changeOrderStatus",HttpMethod.POST,entity,OrderModel.class);
		OrderModel orderModel=res.getBody();
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+orderModel.getCustomerId(),HttpMethod.GET,null,new ParameterizedTypeReference<Integer>(){});
		
	    template.convertAndSend("/notification/"+orderModel.getCustomerId(),res2.getBody());
	    
	    if(roleName.equals("ROLE_TEACHER"))
		{	User userx=us.getUserById(orderModel.getCustomerId());
			orderModel.setCustomerFIO(userx.getFirstname()+" "+userx.getLastname());
		}
		else
		{
			User userx=us.getUserById(orderModel.getFreelancerId());
			orderModel.setFreelancerFIO(userx.getFirstname()+" "+userx.getLastname());
		}
	    
		return new ResponseEntity<>(orderModel,HttpStatus.OK);
	        
	}
	
	@PostMapping("user/changeReiting")
	public ResponseEntity<?> changeReiting(@RequestBody RatingModel model,ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
	    User _user=us.findByEmail(userName);
	    String roleName=_user.getRole().getRoleName();
		HttpEntity<RatingModel> entity=new HttpEntity<>(model);
	    RestTemplate restTemplate = new RestTemplate();
		String host=microservices.getHost();
	    String advPort=microservices.getAdvertismentPort();
	    ResponseEntity<OrderModel> res=restTemplate.exchange("http://"+host+":"+advPort+"/changeRating",HttpMethod.POST,entity,OrderModel.class);
	    OrderModel orderModel=res.getBody();
		ResponseEntity<Double> res1=restTemplate.exchange("http://"+host+":"+advPort+"/rating/"+orderModel.getFreelancerId(),HttpMethod.GET,null,Double.class);
		User user=us.getUserById(orderModel.getFreelancerId());
		if(res1.getBody()!=null)
		{
			user.setReiting(res1.getBody());
			us.saveChanges(user);
		}
		ResponseEntity<Integer> res2=restTemplate.exchange("http://"+host+":"+advPort+"/getMyAllNotificationsSize/"+orderModel.getFreelancerId(),HttpMethod.GET,entity,new ParameterizedTypeReference<Integer>(){});
	    template.convertAndSend("/notification/"+orderModel.getFreelancerId(),res2.getBody());
	    
	    if(roleName.equals("ROLE_TEACHER"))
		{	User userx=us.getUserById(orderModel.getCustomerId());
			orderModel.setCustomerFIO(userx.getFirstname()+" "+userx.getLastname());
		}
		else
		{
			User userx=us.getUserById(orderModel.getFreelancerId());
			orderModel.setFreelancerFIO(userx.getFirstname()+" "+userx.getLastname());
		}
	    
		return new ResponseEntity<>(orderModel,HttpStatus.OK);
	    
	
	}
	
	
}
