package com.mainmicroservice.mainmicroservice;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mainmicroservice.mainmicroservice.Kafka.ConsumerThread; 

@SpringBootApplication
public class MainmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainmicroserviceApplication.class, args);
		
		CountDownLatch latch=new CountDownLatch(1);
	    Thread consumerThread =new Thread(new ConsumerThread(latch,"ports_topic","mainGroupX"));
	    consumerThread.start();
	}

}
