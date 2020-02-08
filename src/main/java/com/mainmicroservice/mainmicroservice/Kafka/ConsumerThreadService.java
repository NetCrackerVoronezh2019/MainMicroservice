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
import org.springframework.stereotype.Service;


@Service
public class ConsumerThreadService {

	@Autowired
	private Microservices micro;
	private KafkaConsumer<String,PortModel> consumer;
	private KafkaConsumer<String,List<String>> consumer2;
	
	// Kafka Topic Name
	@Value("${kafka.porttopicname}")
	private String portTopicName;
	
	// Kafka Group
	@Value("${kafka.porttopicgroup}")
	private String portTopicGroup;
	
	@PostConstruct
	public void init()
	{
		String bootstrapServers2="127.0.0.1:9092";
    	Properties properties=new Properties();
    	properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
    	properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
    	properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,PortModelDeserializer.class.getName());
    	properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    	properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.portTopicGroup);
    	this.consumer=new KafkaConsumer<String,PortModel>(properties);
    	consumer.subscribe(Arrays.asList(this.portTopicName));
    	
    	String topic="rolestopic";
    	Properties properties2=new Properties();
    	properties2.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
    	properties2.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
    	properties2.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,RolesDeserializer.class.getName());
    	properties2.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    	properties2.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group3");
    	this.consumer2=new KafkaConsumer<String,List<String>>(properties2);
    	consumer2.subscribe(Arrays.asList(topic));
	}
	public Runnable getRunnable()
	{
		return new Runnable() {

            public void run() {
            	try {	
        			while(true)
        	    	{
        	    		ConsumerRecords<String,PortModel> records=consumer.poll(Duration.ofMillis(100));	
        	    		for(ConsumerRecord<String,PortModel> recordx:records)
        	    		{
        	    			if(recordx.value().microServiceName==MicroservicesEnum.ADVERTISMENT)
        	    				micro.setAdvertismentmicroserviceport(recordx.value().port);
        	    			else {
        	    				if(recordx.value().microServiceName==MicroservicesEnum.MAIN)
        	    					micro.setMainmicroserviceport(recordx.value().port);
        	    			} 
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
	
	public Runnable getRunnable2()
	{
		return new Runnable() {

            public void run() {
            	try {	
            		while(true)
                	{
                		ConsumerRecords<String,List<String>> records=consumer2.poll(Duration.ofMillis(100));
                		for(ConsumerRecord<String,List<String>> recordx:records)
                		{
                		  System.out.println("Key "+recordx.key()+"      Value "+recordx.value().get(1));	
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
}

