package com.mainmicroservice.mainmicroservice;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mainmicroservice.mainmicroservice.Kafka.ConsumerThread;
import com.mainmicroservice.mainmicroservice.Kafka.PortModel;
import com.mainmicroservice.mainmicroservice.Kafka.Utility; 

@SpringBootApplication
public class MainmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainmicroserviceApplication.class, args);
		
		/*
		PortModel model=new PortModel();
		model.microServiceName="mainmicroservice";
		model.port="8083";	
		Utility.sendPortModelToConfig(model,"http://localhost:7082/setPortModel");
		CountDownLatch latch=new CountDownLatch(1);
	    Thread consumerThread =new Thread(new ConsumerThread(latch,"ports_topic","mainGroup"));
	    consumerThread.start();
		*/
	}

}
