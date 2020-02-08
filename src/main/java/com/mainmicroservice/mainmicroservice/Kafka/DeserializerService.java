package com.mainmicroservice.mainmicroservice.Kafka;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeserializerService {
	
	
	
	@SuppressWarnings("rawtypes")
	public class PortModelDeserializer2 implements Deserializer{
		
		 @Override public void close() {
		 }
		  
		@Override
		 public Object deserialize(String arg0, byte[] arg1) {
		   ObjectMapper mapper = new ObjectMapper();
		    PortModel user = null;
		   try {
		     user = mapper.readValue(arg1, PortModel.class);
		   } catch (Exception e) {
		     e.printStackTrace();
		   }
		   return user;
		 }

		
		@Override
		public void configure(Map configs, boolean isKey) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}
