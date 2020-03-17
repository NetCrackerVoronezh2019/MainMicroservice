package com.mainmicroservice.mainmicroservice.Kafka;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;




@Component
@ConfigurationProperties(prefix="microservices")
public class Microservices {
	
	private String mainPort;
	private String advertismentPort;
	private String amazonPort;
	private String conversationPort;
  
	
	private String main_token;
	private String advertisement_token;
	private String conversation_token;
	private String userAndgroups_token;
	private String amazon_token;
	
	
	
	public String getMain_token() {
		return main_token;
	}
	public void setMain_token(String main_token) {
		this.main_token = main_token;
	}
	public String getAdvertisement_token() {
		return advertisement_token;
	}
	public void setAdvertisement_token(String advertisement_token) {
		this.advertisement_token = advertisement_token;
	}
	public String getConversation_token() {
		return conversation_token;
	}
	public void setConversation_token(String conversation_token) {
		this.conversation_token = conversation_token;
	}
	public String getUserAndgroups_token() {
		return userAndgroups_token;
	}
	public void setUserAndgroups_token(String userAndgroups_token) {
		this.userAndgroups_token = userAndgroups_token;
	}
	public String getAmazon_token() {
		return amazon_token;
	}
	public void setAmazon_token(String amazon_token) {
		this.amazon_token = amazon_token;
	}
	public String getMainPort() {
		return mainPort;
	}
	public void setMainPort(String mainPort) {
		this.mainPort = mainPort;
	}
	public String getAdvertismentPort() {
		return advertismentPort;
	}
	public void setAdvertismentPort(String advertismentPort) {
		this.advertismentPort = advertismentPort;
	}
	public String getAmazonPort() {
		return amazonPort;
	}
	public void setAmazonPort(String amazonPort) {
		this.amazonPort = amazonPort;
	}
	public String getConversationPort() {
		return conversationPort;
	}
	public void setConversationPort(String conversationPort) {
		this.conversationPort = conversationPort;
	}
	
	@Override
	public String toString()
	{
		String ret="main-"+this.getMainPort()+"// advertisement - "+this.getAdvertismentPort()+
				"// amazon- "+this.getAmazonPort()+"// conversation - "+this.getConversationPort();
		return ret;
	}
	
	
	public void setPorts(List<MicroserviceInfo> models)
	{
		for(MicroserviceInfo model:models)
			this.setPort(model);
	}
	
	
	public void setPort(MicroserviceInfo portModel)
	{
		
		if(portModel!=null)
		{
			if(portModel.getMicroserviceName()!=null && portModel.getPort()!=null && portModel.getToken()!=null)
			{
				if(portModel.getMicroserviceName()==MicroservicesEnum.MAIN)
				{
					this.setMainPort(portModel.getPort().toString());
					this.setMain_token(portModel.getToken());
			
				}
				else
				{
					if(portModel.getMicroserviceName()==MicroservicesEnum.ADVERTISEMENT)
					{
						this.setAdvertismentPort(portModel.getPort().toString());
						this.setAdvertisement_token(portModel.getToken());
					}
					else
					{
						if(portModel.getMicroserviceName()==MicroservicesEnum.CONVERSATION)
						{
							this.setConversationPort(portModel.getPort().toString());
							this.setConversation_token(portModel.getToken());
						}
						else
						{
							//this.setUserAndgroupsPort(portModel.getPort().toString());
							//this.setUserAndgroups_token(portModel.getToken());
						}
					}
				}
			}
		}
	}
	
	
	/*
	private void setPort(MicroserviceInfo portModel)
	{
		
		
		if(portModel!=null)
		{
			
			System.out.println(portModel.getMicroserviceName());
			if(portModel.getMicroserviceName()!=null && portModel.getPort()!=null)
			{
				if(portModel.getMicroserviceName()==MicroservicesEnum.MAIN)
					this.setMainPort(portModel.getPort().toString());
				else
				{
					if(portModel.getMicroserviceName()==MicroservicesEnum.ADVERTISEMENT)
						this.setAdvertismentPort(portModel.getPort().toString());
					else
					{
						if(portModel.getMicroserviceName()==MicroservicesEnum.CONVERSATION)
						{
							this.setConversationPort(portModel.getPort().toString());
						}
						else
						{
							this.setAmazonPort(portModel.getPort().toString());
						}
					}
				}
			}
		}
	}
	*/
	
}
