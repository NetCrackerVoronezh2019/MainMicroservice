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

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ConversationController {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
    private UserService us;

	@GetMapping("student/getDialogMembers/{dialogId}")
	public List<UserModel> getDialogMembers(@RequestParam Integer dialogId)
	{
		RestTemplate restTemplate = new RestTemplate();
	    //номер порта Conversation микросервиса(потом это будет автоматически,но сейчас
		// твой мискросервис не отправлеят свой порт в конфиг,ну и еще как то не очень хочется 
		// каждый раз поднять кафку
		String port="8088";
		// пиши свой роут 
		String route="/getDialogMembers/"+dialogId;
		ResponseEntity<List<UserModel>> res=restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<List<UserModel>>(){});
		return res.getBody();
	}
	
	@GetMapping("student/getDialogMessege/{dialogId}")
	public List<MessagesModel> getDialogMessages(@RequestParam Integer dialogId)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getDialogMesseges/"+dialogId;
		ResponseEntity<List<MessagesModel>> res=restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<List<MessagesModel>>(){});
		return res.getBody();
	}
	
	@GetMapping("student/getDialog/{dialogId}")
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
		String route="/getDialog/"+dialogId+"/"+userId;
		ResponseEntity<DialogModel> res=restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<DialogModel>(){});
		return res;
	}
	
	@GetMapping("student/addUserInDialog/{userid}")
	public ResponseEntity<?> addUserInDialog(@RequestParam String userid)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/addUserInDialog/"+userid;
		restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<Object>(){});
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping("student/getUser/{userid}")
	public ResponseEntity<UserModel> getUser(@RequestParam String userid)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getUser/"+userid;
		ResponseEntity<UserModel> res=restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<UserModel>(){});
		return res;
	}
	
	@GetMapping("student/getUserDialogs/{userid}")
	public ResponseEntity<DialogModel> getUserDialogs(@RequestParam String userid)
	{
		RestTemplate restTemplate = new RestTemplate();
		String port="8088";
		String route="/getUserDialogs/"+userid;
		ResponseEntity<DialogModel> res=restTemplate.exchange("http://localhost:"+port+route,HttpMethod.GET,null,new ParameterizedTypeReference<DialogModel>(){});
		return res;
	}
	
	@PostMapping("student/dialogCreate")
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
		restTemplate.exchange("http://localhost:8088/dialogCreate",HttpMethod.POST,entity, Object.class );
		
	    return new ResponseEntity<>(null,HttpStatus.OK);
	}
}
