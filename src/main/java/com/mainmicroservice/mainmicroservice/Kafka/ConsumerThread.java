package com.mainmicroservice.mainmicroservice.Kafka;

import java.util.concurrent.CountDownLatch;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerThread implements Runnable{

	private CountDownLatch latch;
	private KafkaConsumer<String,PortModel> consumer;
	public ConsumerThread(CountDownLatch countDownLatch,String topicName,String groupName)
	{
		this.latch=countDownLatch;
		String bootstrapServers2="127.0.0.1:9092";
    	Properties properties=new Properties();
    	properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers2);
    	properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
    	properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,PortModelDeserializer.class.getName());
    	properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
    	properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
    	KafkaConsumer<String,PortModel> consumer=new KafkaConsumer<String,PortModel>(properties);
    	consumer.subscribe(Arrays.asList(topicName));
	}
	@Override
	public void run() {
		try {
			while(true)
	    	{
	    		ConsumerRecords<String,PortModel> records=consumer.poll(Duration.ofMillis(100));	
	    		for(ConsumerRecord<String,PortModel> recordx:records)
	    		{
	    		  System.out.println("Key "+recordx.key()+"      Value "+recordx.value().port + "portition"+ recordx.partition());	
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
			latch.countDown();
		}
		
	}
  
	public void shutDown()
	{
		consumer.wakeup();
	}
}
