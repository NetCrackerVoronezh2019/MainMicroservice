package com.mainmicroservice.mainmicroservice.Kafka;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mainmicroservice.mainmicroservice.Services.RoleService;


@Service
public class ConsumerThreadService {

	@Autowired
	private Microservices micro;
	
	@Autowired
	private RoleService roleService;
	private KafkaConsumer<String,String> consumer;
	private KafkaConsumer<String,String> consumer2;
	
	// Kafka Topic Name
	@Value("${kafka.porttopicname}")
	private String portTopicName;
	
	// Kafka Group
	@Value("${kafka.porttopicgroup}")
	private String portTopicGroup;
	
	@PostConstruct
	public void init()
	{
		try
		{
			String bootstrapServers2="192.168.99.103:9092";
	    	Properties properties=new Properties();
	    	properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
	    	properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
	    	properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
	    	properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
	    	properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.portTopicGroup);
	    	this.consumer=new KafkaConsumer<String,String>(properties);
	    	consumer.subscribe(Arrays.asList(this.portTopicName));
	    	
	    	
	    	String topic="roles_topic";
	    	Properties properties2=new Properties();
	    	properties2.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
	    	properties2.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
	    	properties2.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
	    	properties2.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
	    	properties2.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "main_roles");
	    	this.consumer2=new KafkaConsumer<String,String>(properties2);
	    	consumer2.subscribe(Arrays.asList(topic));
		}
		catch(Exception ex)
		{
			
		}
	}
	public Runnable getRunnable()
	{
		return new Runnable() {

            public void run() {
            	try {	
        			while(true)
        	    	{
        	    		ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));	
        	    		if(records.count()>0)
                		{
        	    			System.out.println("Microoooooo");
                			RestTemplate template=new RestTemplate();
                			ResponseEntity<List<MicroserviceInfo>> res=template.exchange("http://95.30.222.140:7082/getAllInfo",HttpMethod.GET,null,new ParameterizedTypeReference<List<MicroserviceInfo>>(){});
                			micro.setPorts(res.getBody());
                		}
        	    	}
        		}
        		
        		catch(Exception e)
        		{
        			System.out.println(e.getMessage());
        		}
        		finally
        		{
        			if(consumer!=null)
        			{
        				System.out.println("UNSUBSCRIBE");
	        			consumer.unsubscribe();
	        			consumer.close();
        			}
        			
        		}
            }
        };
	}
	
	public Runnable RoleThread()
	{
		return new Runnable() {

            public void run() {
            	try {	
            		while(true)
                	{
                		ConsumerRecords<String,String> records=consumer2.poll(Duration.ofMillis(100));
                		if(records.count()>0)
                		{
                			RestTemplate template=new RestTemplate();
                			ResponseEntity<List<String>> res=template.exchange("http://95.30.222.140:7082/getAllRoles",HttpMethod.GET,null,new ParameterizedTypeReference<List<String>>(){});
                			roleService.addNewRoles(res.getBody());
                		}
                	}
        		}
        		
        		catch(Exception e)
        		{
        			System.out.println(e.getMessage());
        		}
        		finally
        		{
        			if(consumer2!=null)
        			{   System.out.println("finnaly");
	        			consumer2.unsubscribe();
	        			consumer2.close();
        			}
        			
        		}
            }
        };
	}
}

