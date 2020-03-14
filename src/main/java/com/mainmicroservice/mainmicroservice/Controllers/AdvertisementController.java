package com.mainmicroservice.mainmicroservice.Controllers;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import Models.AdvertisementModel;
import Models.SubjectModel;


@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AdvertisementController {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private Microservices microPorts;
	
	
	@Autowired
	private UserService us;
	
	@RequestMapping(value="addadvertisement",method = RequestMethod.POST)
	public ResponseEntity<String> addAdvertisement(@RequestParam String advertisement,@RequestParam("file") MultipartFile _file, ServletRequest req) throws IOException
	{
		
		  //создаем запрос для отправки файла
		  byte[] somebyteArray=_file.getBytes();
		  HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
	        ContentDisposition contentDisposition = ContentDisposition
	                .builder("form-data")
	                .name("file")
	                .filename("file")
	                .build();
	        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
	        HttpEntity<byte[]> fileEntity = new HttpEntity<>(somebyteArray, fileMap);
	        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	        
	        // получаем токен
	        String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
			User user=us.findByEmail(userName);
			Long id=user.getUserid();
			
			 // JSON TO Java class
			 ObjectMapper mapper = new ObjectMapper();
			 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 AdvertisementModel advertisementModel = mapper.readValue(advertisement,AdvertisementModel.class);
			 advertisementModel.setAuthorId(id);
			 
			//Java class to JSON
			 advertisement = mapper.writeValueAsString(advertisementModel);
	        body.add("file", fileEntity);
	        body.add("advertisement",advertisementModel);
	        HttpEntity<MultiValueMap<String, Object>> requestEntity =new HttpEntity<>(body, headers);
		    RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> res=restTemplate.exchange("http://localhost:1122/student/addadvertisement",HttpMethod.POST,requestEntity, String.class );
			return res;
	}

	@GetMapping("alladvertisements")
	public ResponseEntity<List<AdvertisementModel>> getall() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<AdvertisementModel>> res=restTemplate.exchange("http://localhost:"+"1122"+"/student/alladvertisements",HttpMethod.GET,null,new ParameterizedTypeReference<List<AdvertisementModel>>(){});
	    return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@GetMapping("advertisement/{id}")
	public ResponseEntity<AdvertisementModel> getadv(@PathVariable String id)
	{
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AdvertisementModel> res=restTemplate.exchange("http://localhost:1122/student/advertisement/"+id,HttpMethod.GET,null,new ParameterizedTypeReference<AdvertisementModel>(){});
		res.getBody().setDeadline(LocalDateTime.now());
		return new ResponseEntity<>(res.getBody(),HttpStatus.OK);
	}
	
	@PostMapping("addnewsubject")
	public SubjectModel addNewSubject(@RequestBody SubjectModel _model)
	{
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<SubjectModel> entity = new HttpEntity<SubjectModel>(_model);
		ResponseEntity<SubjectModel> res=restTemplate.exchange("http://localhost:7082/addnewsubject",HttpMethod.POST,entity, SubjectModel.class );
		return res.getBody();
	}
	
	
}
