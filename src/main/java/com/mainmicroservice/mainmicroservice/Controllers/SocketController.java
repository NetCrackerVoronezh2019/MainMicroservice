package com.mainmicroservice.mainmicroservice.Controllers;

import Models.Interfaces.INotification;
import Models.ConversationNotificationModel;
import Models.MessagesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Services.UserService;

import java.util.List;

@Controller
public class SocketController {
    @Autowired
    private SimpMessagingTemplate template;
    
    @Autowired 
    private UserService us;

    @MessageMapping("/sendMessage/")
    @CrossOrigin(origins="http://localhost:4200")
    public void sendMessage(MessagesModel messagesModel) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MessagesModel> entity = new HttpEntity<MessagesModel>(messagesModel);
        ResponseEntity<MessagesModel> messageResponseEntity = restTemplate.exchange("http://localhost:8088/sendMessage/", HttpMethod.POST,entity,new ParameterizedTypeReference<MessagesModel>(){});
        template.convertAndSend("/dialog/" + messagesModel.getDialog(),messagesModel);
        messagesModel= messageResponseEntity.getBody();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8088/getMessageNotifications/").queryParam("messageId", messagesModel.getMessageId());
        ResponseEntity<List<ConversationNotificationModel>> notificationListResponseEntity= restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ConversationNotificationModel>>() {});
        List<ConversationNotificationModel> notifications = notificationListResponseEntity.getBody();
   
        for(ConversationNotificationModel n:notifications)
        {
        	User user=us.findByEmail(n.getUserName());
        	uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8088/getUserNotificationsSize/").queryParam("userId",user.getUserid());
        	ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        	System.out.println("Количество уведомлений - "+res.getBody());
        	template.convertAndSend("/notificationCount/" + n.getUserName(),res.getBody());	
        }
    }
    
}

