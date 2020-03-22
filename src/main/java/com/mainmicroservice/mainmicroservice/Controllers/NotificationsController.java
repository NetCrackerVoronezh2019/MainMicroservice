package com.mainmicroservice.mainmicroservice.Controllers;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class NotificationsController {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
    private UserService us;
	
	@GetMapping("user/getMessageNotificationCount")
	public ResponseEntity<?> getMessageNotificationCount(ServletRequest req)
	{
		
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    if(userName==null)
	    {
	    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	    }
	    
	    RestTemplate restTemplate = new RestTemplate();
		User user=us.findByEmail(userName);
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8088/getUserNotificationsSize/").queryParam("userId",user.getUserid());
    	ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
    	return res;
	}
}
