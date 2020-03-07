package com.mainmicroservice.mainmicroservice.Controllers;

import Models.Interfaces.INotification;
import Models.MessagesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class SocketController {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/sendMessage/")
    @CrossOrigin(origins="http://localhost:4200")
    public void sendMessage(MessagesModel messagesModel) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MessagesModel> entity = new HttpEntity<MessagesModel>(messagesModel);
        ResponseEntity<MessagesModel> messageResponseEntity = restTemplate.exchange("http://localhost:8088/sendMessage/", HttpMethod.POST,entity,new ParameterizedTypeReference<MessagesModel>(){});
        template.convertAndSend("/dialog/" + messagesModel.getDialog(),messagesModel);
        MessagesModel messagesModel1= messageResponseEntity.getBody();
        ResponseEntity<List<INotification>> notificationListResponseEntity= restTemplate.exchange("http://localhost:8088/getMessageNotifications/", HttpMethod.GET, null, new ParameterizedTypeReference<List<INotification>>() {});
        List<INotification> notifications = notificationListResponseEntity.getBody();
        for (int i = 0; i < notifications.size(); i++) {
            template.convertAndSend("/notification/" + notifications.get(i).getUserName(),notifications.get(i));
        }
    }

}

