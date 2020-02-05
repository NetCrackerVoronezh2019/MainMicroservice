package com.mainmicroservice.mainmicroservice.Controllers;

import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.AdvertisementModel;
import Models.AdvertisementModel2;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdvertisementController {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserService us;
	
	@PostMapping("student/addadvertisement")
	public void  addAdvertisement(@RequestBody AdvertisementModel model, ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		Long id=user.getUserid();
		model.setUserId(id);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AdvertisementModel> entity = new HttpEntity<AdvertisementModel>(model);
		restTemplate.exchange("http://localhost:1122/student/addadvertisement",HttpMethod.POST,entity, AdvertisementModel.class );
	}
	
	@GetMapping("student/alladvertisements")
	public List<AdvertisementModel2> getall() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<AdvertisementModel2>> res=restTemplate.exchange("http://localhost:1122/student/alladvertisements",HttpMethod.GET,null,new ParameterizedTypeReference<List<AdvertisementModel2>>(){});
		return res.getBody();
	}
	
}
