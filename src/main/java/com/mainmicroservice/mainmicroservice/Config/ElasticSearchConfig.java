package com.mainmicroservice.mainmicroservice.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableJpaRepositories(basePackages = "com.mainmicroservice.mainmicroservice.Repositories")
@EnableElasticsearchRepositories(basePackages="com.mainmicroservice.mainmicroservice.ElasticRepositorys")
public class ElasticSearchConfig {

	 @Bean
	    public Client client() throws UnknownHostException {
	    	
	    	 Settings esSettings = Settings.builder()
	                 .put("cluster.name", "mycluster")

	                 //.put("node.name","4ca16122605d")
	                 .build();
	    	 
	    	@SuppressWarnings("resource")
			TransportClient client = new PreBuiltTransportClient(esSettings)
	    	        .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.99.101"), 9300));
	    return client;
	    }

	    @Bean
	    public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
	        return new ElasticsearchTemplate(client(), new ElasticCustomEntityMapper());
	    }
	    
	    private class ElasticCustomEntityMapper implements EntityMapper {

	        private ObjectMapper mapper;

	        @Autowired
	        private ElasticCustomEntityMapper() {
	            this.mapper = new ObjectMapper();
	            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	            mapper.registerModule(new JavaTimeModule());
	        }

	        @Override
	        public String mapToString(Object object) throws IOException {
	            return mapper.writeValueAsString(object);
	        }

	        @Override
	        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
	            return mapper.readValue(source, clazz);
	        }

			@Override
			public Map<String, Object> mapObject(Object source) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
				// TODO Auto-generated method stub
				return null;
			}

	    }
}
