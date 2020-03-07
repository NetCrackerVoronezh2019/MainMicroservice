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
	
	
	public void setPorts(List<PortModel> models)
	{
		for(PortModel model:models)
			this.setPort(model);
	}
	
	
	private void setPort(PortModel portModel)
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
	
	
}
