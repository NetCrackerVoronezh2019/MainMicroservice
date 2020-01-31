package com.mainmicroservice.mainmicroservice.Kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class Utility {

	@Value("${server.port}")
	private String port;
	
	
	public void sendPortModelToConfig(String configURL)
	{
		    RestTemplate restTemplate = new RestTemplate();
		    PortModel model=new PortModel();
		    model.microServiceName=MicroservicesEnum.MAIN;
		    model.port=this.port;
			HttpEntity<PortModel> entity = new HttpEntity<PortModel>(model);
			ResponseEntity<PortModel> response = restTemplate.exchange(configURL,HttpMethod.POST,entity, PortModel.class );
	}
}
