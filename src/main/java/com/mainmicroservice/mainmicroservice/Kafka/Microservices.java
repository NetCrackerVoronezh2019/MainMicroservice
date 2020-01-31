package com.mainmicroservice.mainmicroservice.Kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="microservices")
public class Microservices {
	
	private String mainmicroserviceport;
	private String advertismentmicroserviceport;
	
	public String getMainmicroserviceport() {
		return mainmicroserviceport;
	}
	public void setMainmicroserviceport(String mainmicroserviceport) {
		this.mainmicroserviceport = mainmicroserviceport;
	}
	public String getAdvertismentmicroserviceport() {
		return advertismentmicroserviceport;
	}
	public void setAdvertismentmicroserviceport(String advertismentmicroserviceport) {
		this.advertismentmicroserviceport = advertismentmicroserviceport;
	}
	
	

}
