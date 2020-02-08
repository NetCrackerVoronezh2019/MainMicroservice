package com.mainmicroservice.mainmicroservice.Kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RolesDeserializer implements Deserializer<List<String>> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> deserialize(String topic, byte[] data) {
		 ObjectMapper mapper = new ObjectMapper();
		    ArrayList<String> user = null;
		   try {
		     user = (ArrayList<String>)mapper.readValue(data,ArrayList.class);
		   } catch (Exception e) {
		     e.printStackTrace();
		   }
		   return user;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
