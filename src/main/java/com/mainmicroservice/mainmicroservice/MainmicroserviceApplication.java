package com.mainmicroservice.mainmicroservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import com.mainmicroservice.mainmicroservice.Kafka.ConsumerThreadService;
import com.mainmicroservice.mainmicroservice.Kafka.Utility;



@SpringBootApplication
public class MainmicroserviceApplication {
	
	public static void main(String[] args) {
		
		ConfigurableApplicationContext app=SpringApplication.run(MainmicroserviceApplication.class, args);
		
		ConsumerThreadService a = (ConsumerThreadService) app.getBean("consumerThreadService");		
	     //	Utility utility=(Utility) app.getBean("utility");	
	    //   utility.sendPortModelToConfig("http://localhost:7082/setPortModel");
	   // Thread consumerThread =new Thread(a.getRunnable());
	    //consumerThread.start();
	   // Thread RoleThread =new Thread(a.RoleThread());
	   // RoleThread.start();
	    
	}
}
