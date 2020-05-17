package com.mainmicroservice.mainmicroservice.Controllers;

import Models.CleanConversationNotificationModel;
import Models.ConversationNotificationModel;
import Models.MessagesModel;
import Models.NotificationModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SocketController {
	
    @Autowired
    private SimpMessagingTemplate template;
    
    @Autowired 
    private UserService us;
    
    @Autowired
	private JwtTokenProvider tokenProvider;
    
    @Autowired
	private Microservices microservices;

    @PostMapping("/sendMessage/")
    public ResponseEntity<?>  sendMessage(@RequestBody MessagesModel messagesModel) {
    	
    	String port=this.microservices.getConversationPort();
		String host=this.microservices.getHost();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MessagesModel> entity = new HttpEntity<MessagesModel>(messagesModel);
        ResponseEntity<MessagesModel> messageResponseEntity = restTemplate.exchange("http://"+host+":"+port+"/sendMessage/", HttpMethod.POST,entity,new ParameterizedTypeReference<MessagesModel>(){});
        messagesModel= messageResponseEntity.getBody();
        template.convertAndSend("/dialog/" + messagesModel.getDialog(),messagesModel);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getMessageNotifications/").queryParam("messageId", messagesModel.getMessageId());
        ResponseEntity<List<ConversationNotificationModel>> notificationListResponseEntity= restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ConversationNotificationModel>>() {});
        List<ConversationNotificationModel> notifications = notificationListResponseEntity.getBody();
   
        for(ConversationNotificationModel n:notifications)
        {
        	uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getUserNotificationsSize/").queryParam("userId",n.getUserId());
        	ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        	template.convertAndSend("/notificationCount/" + n.getUserId(),res.getBody());
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    
    @MessageMapping("/sendNotification/")
    @CrossOrigin(origins="http://localhost:4200")
    public void sendNotification(NotificationModel model) {
    	
    	String port=this.microservices.getAdvertismentPort();
		String host=this.microservices.getHost();
    	RestTemplate restTemplate = new RestTemplate();
        HttpEntity<NotificationModel> entity = new HttpEntity<NotificationModel>(model);
        ResponseEntity<Object> messageResponseEntity = restTemplate.exchange("http://"+host+":"+port+"/newNotification", HttpMethod.POST,entity,new ParameterizedTypeReference<Object>(){});
        template.convertAndSend("/notification/",1006);
        System.out.println(model.toString());
    }

    @MessageMapping("dialog/cleanNotifications")
    @CrossOrigin(origins="http://localhost:4200")
    public void cleanNotifications(CleanConversationNotificationModel cleanConversationNotificationModel) {
    	
    	String port=this.microservices.getConversationPort();
		String host=this.microservices.getHost();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/cleanUserNotifications/").queryParam("userId",cleanConversationNotificationModel.getUserId()).
                                                                                                                            queryParam("dialogId",cleanConversationNotificationModel.getDialogId());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
        uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getUserNotificationsSize/").queryParam("userId",cleanConversationNotificationModel.getUserId());
        ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        template.convertAndSend("/notificationCount/" + cleanConversationNotificationModel.getUserId(),res.getBody());
        User user = us.getUserById(cleanConversationNotificationModel.getUserId());
        template.convertAndSend("/readMessages/" + cleanConversationNotificationModel.getDialogId(),user);
    }

}

