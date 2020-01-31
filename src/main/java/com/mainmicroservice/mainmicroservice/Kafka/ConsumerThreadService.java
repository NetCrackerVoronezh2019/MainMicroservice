package com.mainmicroservice.mainmicroservice.Kafka;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsumerThreadService {

	@Autowired
	private Microservices micro;
	private KafkaConsumer<String,PortModel> consumer;
	@Value("${kafka.porttopicname}")
	private String portTopicName;
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
        		
        		catch(WakeupException e)
        		{
        			System.out.println(e.getMessage()+" ------WakeupException");
        		}
        		finally
        		{
        			consumer.close();
        			
        		}
            }
        };
	}
}
