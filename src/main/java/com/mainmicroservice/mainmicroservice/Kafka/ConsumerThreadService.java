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
		String bootstrapServers2="192.168.99.100:9092";
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
                			RestTemplate template=new RestTemplate();
                			ResponseEntity<List<PortModel>> res=template.exchange("http://localhost:7082/getallports",HttpMethod.GET,null,new ParameterizedTypeReference<List<PortModel>>(){});
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
        			consumer.close();
        			
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
                			ResponseEntity<List<String>> res=template.exchange("http://localhost:7082/getallroles",HttpMethod.GET,null,new ParameterizedTypeReference<List<String>>(){});
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
        			consumer2.close();
        			
        		}
            }
        };
	}
}

