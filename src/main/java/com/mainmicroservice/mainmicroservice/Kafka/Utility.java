package com.mainmicroservice.mainmicroservice.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;


@Component
public class Utility {

	@Value("${server.port}")
	private String port;
	
	@Autowired
	private JwtTokenProvider jwtProvider;
	
	
	public void sendInfoModelToConfig(String configURL)
	{
		    RestTemplate restTemplate = new RestTemplate();
		    MicroserviceInfo model=new MicroserviceInfo();
		    model.setMicroserviceName(MicroservicesEnum.MAIN);
		    model.setPort(Integer.parseInt(this.port));
		    model.setToken(jwtProvider.createTokenForMicroservice());
			HttpEntity<MicroserviceInfo> entity = new HttpEntity<MicroserviceInfo>(model);
			System.out.println("Send port");
			ResponseEntity<MicroserviceInfo> response = restTemplate.exchange(configURL,HttpMethod.POST,entity, MicroserviceInfo.class );
	}
}
