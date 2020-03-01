package com.mainmicroservice.mainmicroservice.Controllers;


	
	
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import Models.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ConversationController {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
    private UserService us;

	@GetMapping("student/getDialogMembers/")
	public List<UserModel> getDialogMembers(@RequestParam Integer dialogId,ServletRequest req)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getDialogMembers/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("dialogId", dialogId).queryParam("userId", userId);
		ResponseEntity<List<UserModel>> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<List<UserModel>>(){});
		return res.getBody();
	}
	
	@GetMapping("student/getDialogMessages/")
	public List<MessagesModel> getDialogMessages(@RequestParam Integer dialogId, ServletRequest req)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getDialogMessages/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("dialogId", dialogId).queryParam("userId", userId);
		ResponseEntity<List<MessagesModel>> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<List<MessagesModel>>(){});
		return res.getBody();
	}
	
	@GetMapping("student/getDialog")
	public ResponseEntity<DialogModel> getDialog(@RequestParam Integer dialogId, ServletRequest req)
	{
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    if(userName==null)
	    {
	    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	    }

	    User user=us.findByEmail(userName);
	    Long userId=user.getUserid();
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getDialog/";
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("dialogId", dialogId).queryParam("userId", userId);
		ResponseEntity<DialogModel> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<DialogModel>(){});
		return res;
	}
	
	@GetMapping("student/addUserInDialog/")
	public ResponseEntity<?> addUserInDialog(@RequestParam String userName,@RequestParam Integer dialogId, ServletRequest req)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/addUserInDialog/";
		String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(adderName);
		int adderId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("adderId", adderId).queryParam("userName",userName).queryParam("dialogId",dialogId);
		restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<Object>(){});
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping("student/getUser/")
	public ResponseEntity<UserModel> getUser(ServletRequest req)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getUser/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("userId", userId);
		ResponseEntity<UserModel> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<UserModel>(){});
		return res;
	}
	
	@GetMapping("student/getUserDialogs/")
	public ResponseEntity<List<DialogModel>> getUserDialogs(ServletRequest req)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getUserDialogs/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("userId", userId);
		ResponseEntity<List<DialogModel>> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<List<DialogModel>>(){});
		return res;
	}
	
	@PostMapping("student/dialogCreate/")
	public ResponseEntity<?> dialogCreate(@RequestBody DialogModel dialog, ServletRequest req)
	{
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
	    if(userName==null)
	    {
	    	return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	    }
	    
	    User user=us.findByEmail(userName);
	    // у меня userId - Long у тебя Int
	    int userId=(int)(long)user.getUserid();
	    
	    dialog.setCreatorId(userId);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<DialogModel> entity = new HttpEntity<DialogModel>(dialog);
		restTemplate.exchange("http://localhost:8088/dialogCreate/",HttpMethod.POST,entity, Object.class );
		
	    return new ResponseEntity<>(null,HttpStatus.OK);
	}

	@DeleteMapping("student/liveDialog/")
	public  ResponseEntity<?> liveDialog(@RequestParam(name = "dialogId") Integer dialogId,ServletRequest req) {
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/liveDialog/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("dialogId", dialogId).queryParam("userId", userId);
		ResponseEntity<?> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.DELETE,null,Object.class);
		return res;
	}

	@DeleteMapping("student/deleteDialog/")
	public  ResponseEntity<?> deleteDialog(@RequestParam(name = "dialogId") Integer dialogId,ServletRequest req) {
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/deleteDialog/";
		String userName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		int userId=(int)(long)user.getUserid();
		UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:"+port+route).queryParam("dialogId", dialogId).queryParam("userId", userId);
		ResponseEntity<?> res=restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.DELETE,null,Object.class);
		return res;
	}
}

