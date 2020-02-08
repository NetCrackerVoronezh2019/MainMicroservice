package com.mainmicroservice.mainmicroservice;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;


import com.mainmicroservice.mainmicroservice.Kafka.ConsumerThreadService;
import com.mainmicroservice.mainmicroservice.Kafka.PortModel;
import com.mainmicroservice.mainmicroservice.Kafka.RolesDeserializer;
import com.mainmicroservice.mainmicroservice.Kafka.Utility;



@SpringBootApplication
public class MainmicroserviceApplication {
	
	public static void main(String[] args) {
		
		ConfigurableApplicationContext app=SpringApplication.run(MainmicroserviceApplication.class, args);

		ConsumerThreadService a = (ConsumerThreadService) app.getBean("consumerThreadService");		
		Utility utility=(Utility) app.getBean("utility");	
	    utility.sendPortModelToConfig("http://localhost:7082/setPortModel");
	    Thread consumerThread =new Thread(a.getRunnable());
	    consumerThread.start();
	    Thread consumerThread2 =new Thread(a.getRunnable2());
	    consumerThread2.start();
	    
		
		
		
	}

}
