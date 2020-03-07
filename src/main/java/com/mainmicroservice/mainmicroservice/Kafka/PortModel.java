package com.mainmicroservice.mainmicroservice.Kafka;


public class PortModel {


	private MicroservicesEnum microserviceName;
	private Integer port;
	
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
	
	
	
}
