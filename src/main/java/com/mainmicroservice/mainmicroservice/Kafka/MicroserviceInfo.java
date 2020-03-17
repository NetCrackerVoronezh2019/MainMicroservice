package com.mainmicroservice.mainmicroservice.Kafka;


public class MicroserviceInfo {


	private MicroservicesEnum microserviceName;
	private Integer port;
	private String token;
	
	public MicroservicesEnum getMicroserviceName() {
		return microserviceName;
	}
	public void setMicroserviceName(MicroservicesEnum microserviceName) {
		this.microserviceName = microserviceName;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
	
	
	
	
}
