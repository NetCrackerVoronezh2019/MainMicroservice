package com.mainmicroservice.mainmicroservice.Controllers;

import Models.GroupModel;
import Models.UserAndGroupUserModel;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class UserAndGroupController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService us;

    @PostMapping("userAndGroup/createGroup/")
    public void createGroup(@RequestBody GroupModel groupModel, ServletRequest req) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/createGroup/").queryParam("creatorId",user.getUserid()).queryParam("groupName",groupModel.getName());
        ResponseEntity<Long> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, new ParameterizedTypeReference<Long>() {});
    }

    @PostMapping("userAndGroup/subscribe")
    public void subscribe(ServletRequest req,@RequestBody GroupModel groupModel) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/group/subscribe").queryParam("userId",user.getUserid()).queryParam("groupId",groupModel.getGroupId());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, Object.class);
    }

    @GetMapping("userAndGroup/getGroup")
    public GroupModel getGroup(@RequestParam Long groupId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/getGroup").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<GroupModel>(){}).getBody();
    }
    @GetMapping("userAndGroup/getGroupUsers")
    public List<UserAndGroupUserModel> getGroupUsers(@RequestParam Long groupId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/getGroupUsers").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>(){}).getBody();
    }

    @DeleteMapping("userAndGroup/leaveGroup/")
    public void leaveGroup(ServletRequest req,@RequestParam Long groupId) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/leaveGroup/").queryParam("userId",user.getUserid()).queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @DeleteMapping("userAndGroup/deleteGroup")
    public void deleteGroup(ServletRequest req, @RequestBody Long groupId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/deleteGroup").queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @GetMapping("userAndGroup/getThisUser")
    public UserAndGroupUserModel getUser(ServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/getUser").queryParam("userId",user.getUserid());
        ResponseEntity<UserAndGroupUserModel> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<UserAndGroupUserModel>(){});
        return res.getBody();
    }

    @GetMapping("userAndGroup/getUser")
    public UserAndGroupUserModel getThisUser(@RequestParam(name = "userId") Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/getUser").queryParam("userId",userId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<UserAndGroupUserModel>(){}).getBody();
    }

    @PostMapping("userAndGroup/startDialogWithUser")
    public void startDialog(@RequestParam Long userId,ServletRequest req) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/startDialogWithUser").queryParam("creatorId",user.getUserid()).queryParam("userId",userId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, Object.class);
    }

    @GetMapping("group/search")
    public List<GroupModel> search(@RequestParam String name) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/group/search").queryParam("name",name);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){}).getBody();
    }

    @GetMapping("groups/getThisUserGroups")
    public List<GroupModel> getThisUserGroups(ServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/groups/getUserGroups").queryParam("userId",user.getUserid());
        ResponseEntity<List<GroupModel>> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){});
        return res.getBody();
    }

    @GetMapping("groups/getUserGroups")
    public List<GroupModel> getUserGroups(@RequestParam Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://localhost:8090/groups/getUserGroups").queryParam("userId", userId);
        ResponseEntity<List<GroupModel>> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){});
        return res.getBody();
    }
}
