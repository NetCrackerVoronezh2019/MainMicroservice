package com.mainmicroservice.mainmicroservice.Kafka;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class Utility {

	public static void sendPortModelToConfig(PortModel portModel,String configURL)
	{
		    RestTemplate restTemplate = new RestTemplate();
			HttpEntity<PortModel> entity = new HttpEntity<PortModel>(portModel);
			ResponseEntity<PortModel> response = restTemplate.exchange(configURL,HttpMethod.POST,entity, PortModel.class );
	}
}
