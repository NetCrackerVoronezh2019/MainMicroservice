package com.mainmicroservice.mainmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mainmicroservice.mainmicroservice.Kafka.KafkaCons;

@SpringBootApplication
public class MainmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainmicroserviceApplication.class, args);
		KafkaCons.getPortModel("ports_topic", "mainMicroGroup");
	}

}
