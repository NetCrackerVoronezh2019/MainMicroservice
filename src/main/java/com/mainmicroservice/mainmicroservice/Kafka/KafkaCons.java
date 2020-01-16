package com.mainmicroservice.mainmicroservice.Kafka;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;



public class KafkaCons {
	
	public static void getPortModel(String topicName,String groupName)
	{
		String bootstrapServers2="127.0.0.1:9092";
		String topic=topicName;
    	Properties properties2=new Properties();
    	properties2.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
    	properties2.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
    	properties2.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,PortModelDeserializer.class.getName());
    	properties2.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    	properties2.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
    	KafkaConsumer<String,PortModel> consumer=new KafkaConsumer<String,PortModel>(properties2);
    	consumer.subscribe(Arrays.asList(topic));
    	
    	while(true)
    	{
    		ConsumerRecords<String,PortModel> records=consumer.poll(Duration.ofMillis(100));
    		
    		for(ConsumerRecord<String,PortModel> recordx:records)
    		{
    		  System.out.println("Key "+recordx.key()+"      Value "+recordx.value().port + "portition"+ recordx.partition());	
    		}
    	}
    	
	}

}
