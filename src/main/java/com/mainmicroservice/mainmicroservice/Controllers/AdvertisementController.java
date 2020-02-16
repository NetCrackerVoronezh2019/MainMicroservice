package com.mainmicroservice.mainmicroservice.Controllers;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
	
	@PostMapping("student/addadvertisement")
	public ResponseEntity<?> addAdvertisement(@RequestBody AdvertisementModel model, ServletRequest req)
	{
		String userName=this.tokenProvider.getUsername((HttpServletRequest) req);
		User user=us.findByEmail(userName);
		Long id=user.getUserid();
		model.setAuthorId(id);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<AdvertisementModel> entity = new HttpEntity<AdvertisementModel>(model);
		restTemplate.exchange("http://localhost:1122/student/addadvertisement",HttpMethod.POST,entity, AdvertisementModel.class );
		 return new ResponseEntity<>(HttpStatus.OK);
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
