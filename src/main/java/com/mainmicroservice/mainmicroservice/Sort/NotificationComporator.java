package com.mainmicroservice.mainmicroservice.Sort;

import java.util.Comparator;

import Models.FullNotificationModel;

public class NotificationComporator implements Comparator<FullNotificationModel>{
 
    public int compare(FullNotificationModel a, FullNotificationModel b){
     
        return b.getNotification().getDate().compareTo(a.getNotification().getDate());
    }
}