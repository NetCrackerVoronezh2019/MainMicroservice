package com.mainmicroservice.mainmicroservice.Kafka;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PortModelDeserializer implements Deserializer<PortModel>{
	
	 @Override public void close() {
	 }
	  
	@Override
	 public PortModel deserialize(String arg0, byte[] arg1) {
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