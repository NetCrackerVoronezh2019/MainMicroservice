package com.mainmicroservice.mainmicroservice.Controllers;

import Models.*;
import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Kafka.Microservices;
import com.mainmicroservice.mainmicroservice.Security.JwtTokenProvider;
import com.mainmicroservice.mainmicroservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Microservices microservices;


    @PutMapping("groups/settings")
    public void groupSettings(@RequestBody GroupModel groupModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<GroupModel> groupModelHttpEntity = new HttpEntity<>(groupModel);
        restTemplate.exchange("http://"+host+":"+port+"/groupSettings", HttpMethod.PUT,groupModelHttpEntity, Object.class );
    }

    @GetMapping("groups/admins")
    public List<UserAndGroupUserModel> getAdmins(@RequestParam Long groupId) {

        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/admins").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>() {}).getBody();
    }

    @PutMapping("groups/makeAdmin")
    public void makeAdmin(@RequestParam Long groupId,@RequestParam Long userId){
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"groups/makeAdmin").queryParam("groupId",groupId).queryParam("userId",userId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
    }

    @PutMapping("groups/deleteAdmin")
    public void deleteAdmin(@RequestParam Long groupId,@RequestParam Long userId){
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/deleteAdmin").queryParam("groupId",groupId).queryParam("userId",userId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
    }

    @PostMapping("comments/send")
    public void sendComment(@RequestBody CommentModel commentModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<CommentModel> commentModelHttpEntity = new HttpEntity<>(commentModel);
        restTemplate.exchange("http://"+host+":"+port+"/SendComment", HttpMethod.POST,commentModelHttpEntity, Object.class );
    }

    @DeleteMapping("comments/delete")
    public void deleteComment(@RequestParam Long commentId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/deleteComment").queryParam("commentId",commentId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @PutMapping("comments/redact")
    public void redactComment(@RequestBody CommentModel commentModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<CommentModel> commentModelHttpEntity = new HttpEntity<>(commentModel);
        restTemplate.exchange("http://"+host+":"+port+"/RedactComment", HttpMethod.PUT,commentModelHttpEntity, Object.class );
    }

    @GetMapping("posts/getComments")
    public List<CommentModel> getComments(@RequestParam Long postId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getComments").queryParam("postId",postId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<CommentModel>>() {}).getBody();
    }

    @PostMapping("groups/makePost")
    public void makePost(@RequestBody PostModel postModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PostModel> postModelHttpEntity = new HttpEntity<>(postModel);
        List<GroupNotificationsModel> res = restTemplate.exchange("http://"+host+":"+port+"groups/makePost", HttpMethod.POST,postModelHttpEntity, new ParameterizedTypeReference<List<GroupNotificationsModel>>() {}).getBody();
        for (GroupNotificationsModel not:
                res) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/getNotificationsCount").queryParam("userId", not.getUserid());
            ResponseEntity<String> c = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
            });
            template.convertAndSend("/groupsNot/" + not.getUserid(),c.getBody());
        }
    }

    @PutMapping("posts/redact")
    public void redactPost(@RequestBody PostModel postModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<PostModel> postModelHttpEntity = new HttpEntity<>(postModel);
        restTemplate.exchange("http://"+host+":"+port+"/postSettings", HttpMethod.PUT,postModelHttpEntity, Object.class );
    }

    @DeleteMapping("posts/delete")
    public void deletePost(@RequestParam Long postId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/deletePost").queryParam("postId",postId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @GetMapping("groups/getPosts")
    public List<PostModel> getPosts(@RequestParam Long groupId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/getPosts").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<PostModel>>() {}).getBody();
    }

    @PostMapping("userAndGroup/createGroup/")
    public void createGroup(@RequestBody GroupModel groupModel, ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        groupModel.setCreatorId(user.getUserid());
        HttpEntity<GroupModel> groupModelHttpEntity = new HttpEntity<>(groupModel);
        restTemplate.exchange("http://"+host+":"+port+"/createGroup/", HttpMethod.POST, groupModelHttpEntity, Object.class);
    }

    @PutMapping("users/settings")
    public void userSettings(@RequestBody UserAndGroupUserModel userModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserAndGroupUserModel> userAndGroupUserModelHttpEntity = new HttpEntity<>(userModel);
        restTemplate.exchange("http://"+host+":"+port+"/userSettings", HttpMethod.PUT,userAndGroupUserModelHttpEntity, Object.class );
    }

    @PostMapping("users/startDialog")
    public Long startPrivateDialog(@RequestParam Long userId, ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/startDialogWithUser").queryParam("userId",userId).queryParam("creatorId",user.getUserid());
        ResponseEntity<Long> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, new ParameterizedTypeReference<Long>() {});
        return res.getBody();
    }

    @GetMapping("user/friends")
    public List<UserAndGroupUserModel> getFriends(@RequestParam Long userId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/friends").queryParam("userId",userId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>() {}).getBody();
    }

    @GetMapping("thisUser/friends")
    public List<UserAndGroupUserModel> getThisUserFriends(ServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/friends").queryParam("userId",user.getUserid());
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>() {}).getBody();
    }

    @GetMapping("thisUser/ingoingFriends")
    public List<UserAndGroupUserModel> getIngoingFriends(ServletRequest req) {

        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/IngoingFriends").queryParam("userId",user.getUserid());
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>() {}).getBody();
    }

    @GetMapping("thisUser/outgoingFriends")
    public List<UserAndGroupUserModel> getOutgoingFriends(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/OutgoingFriends").queryParam("userId",user.getUserid());
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>() {}).getBody();
    }


    @PostMapping("userAndGroup/subscribe")
    public void subscribe(ServletRequest req,@RequestBody GroupModel groupModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/subscribe").queryParam("userId",user.getUserid()).queryParam("groupId",groupModel.getGroupId());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, Object.class);
    }

    @GetMapping("userAndGroup/getGroup")
    public GroupModel getGroup(@RequestParam Long groupId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getGroup").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<GroupModel>(){}).getBody();
    }
    @GetMapping("userAndGroup/getGroupUsers")
    public List<UserAndGroupUserModel> getGroupUsers(@RequestParam Long groupId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getGroupUsers").queryParam("groupId",groupId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>(){}).getBody();
    }

    @DeleteMapping("userAndGroup/leaveGroup/")
    public void leaveGroup(ServletRequest req,@RequestParam Long groupId) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/leaveGroup/").queryParam("userId",user.getUserid()).queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @DeleteMapping("userAndGroup/deleteGroup")
    public void deleteGroup(ServletRequest req, @RequestBody Long groupId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/deleteGroup").queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @GetMapping("userAndGroup/getThisUser")
    public UserAndGroupUserModel getUser(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getUser").queryParam("userId",user.getUserid());
        ResponseEntity<UserAndGroupUserModel> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<UserAndGroupUserModel>(){});
        return res.getBody();
    }

    @GetMapping("userAndGroup/getUser")
    public UserAndGroupUserModel getThisUser(@RequestParam(name = "userId") Long userId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getUser").queryParam("userId",userId);
        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<UserAndGroupUserModel>(){}).getBody();
    }

    @PostMapping("userAndGroup/startDialogWithUser")
    public void startDialog(@RequestParam Long userId,ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/startDialogWithUser").queryParam("creatorId",user.getUserid()).queryParam("userId",userId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, null, Object.class);
    }

    @GetMapping("group/search")
    public List<GroupModel> search(@RequestParam String name, @RequestParam String subjectName) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/search").queryParam("name",name).queryParam("subjectName",subjectName);
        return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){}).getBody();
    }

    @GetMapping("groups/getThisUserGroups")
    public List<GroupModel> getThisUserGroups(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/getUserGroups").queryParam("userId",user.getUserid());
        ResponseEntity<List<GroupModel>> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){});
        return res.getBody();
    }

    @GetMapping("groups/getUserGroups")
    public List<GroupModel> getUserGroups(@RequestParam Long userId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/getUserGroups").queryParam("userId", userId);
        ResponseEntity<List<GroupModel>> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GroupModel>>(){});
        return res.getBody();
    }

    @GetMapping("groups/getAllSubjects")
    public List<SubjectModel> getAllSubjects() {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<SubjectModel>> res=restTemplate.exchange("http://"+host+":"+port+"/subjects/getAll",HttpMethod.GET,null,new ParameterizedTypeReference<List<SubjectModel>>(){});
        return res.getBody();
    }

    @GetMapping("group/searchUsers")
    public List<UserAndGroupUserModel> searchUsersInGroup(@RequestParam Long groupId, @RequestParam String firstName, @RequestParam String lastName) {
        RestTemplate restTemplate = new RestTemplate();
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/groups/searchUser").queryParam("groupId",groupId).queryParam("firstName",firstName).queryParam("lastName",lastName);
        return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>(){}).getBody();
    }

    @GetMapping("users/search")
    public List<UserAndGroupUserModel> userSearch(@RequestParam String firstName, @RequestParam String lastName) {
        RestTemplate restTemplate = new RestTemplate();
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/user/search").queryParam("firstName",firstName).queryParam("lastName",lastName);
        return restTemplate.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<UserAndGroupUserModel>>(){}).getBody();
    }

    @PutMapping("friend/add")
    public void addFriend(@RequestParam Long ingoingId, ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/addToFriends").queryParam("ingoingId",ingoingId).queryParam("outgoingId",user.getUserid());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
        uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/user/countNotifications").queryParam("userId", ingoingId);
        ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null, new ParameterizedTypeReference<String>(){});
        template.convertAndSend("/friends/" + ingoingId, res.getBody());
    }

    @PutMapping("friend/remove")
    public void removeFriend(@RequestParam Long ingoingId, ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/removeFriend").queryParam("ingoingId",ingoingId).queryParam("outgoingId",user.getUserid());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
        uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/user/countNotifications").queryParam("userId", ingoingId);
        ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null, new ParameterizedTypeReference<String>(){});
        template.convertAndSend("/friends/" + ingoingId, res.getBody());
    }

    @GetMapping("user/friendsNotifications")
    public String getFriendsNot(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/user/countNotifications").queryParam("userId", user.getUserid());
        ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null, new ParameterizedTypeReference<String>(){});
        return res.getBody();
    }

    @DeleteMapping("user/cleanNotifications")
    public void cleanNotifications(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/user/cleanNotifications").queryParam("userId",user.getUserid());
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
    }

    @PutMapping("group/setAvatar")
    public void setAvatar(@RequestBody GroupModel groupModel) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<GroupModel> groupModelHttpEntity = new HttpEntity<>(groupModel);
        restTemplate.exchange("http://"+host+":"+port+"/group/setAvatar", HttpMethod.PUT,groupModelHttpEntity, Object.class );
    }

    @PutMapping("group/notificationsOn")
    public void notificationsOn(ServletRequest req,@RequestParam Long groupId) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/notificationOn").queryParam("userId",user.getUserid()).queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
    }

    @PutMapping("group/notificationsOff")
    public void notificationsOff(ServletRequest req,@RequestParam Long groupId) {
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        User user=us.findByEmail(adderName);
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/notificationsOff").queryParam("userId",user.getUserid()).queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, null, Object.class);
    }

    @GetMapping("groups/notificationsCount")
    public String groupNotifications(ServletRequest req) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        String adderName = this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user = us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/getNotificationsCount").queryParam("userId", user.getUserid());
        ResponseEntity<String> res = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });
        return res.getBody();
    }

    @DeleteMapping("group/cleanNotifications")
    public void cleanGroupNotifications(ServletRequest req, @RequestParam Long groupId) {
        RestTemplate restTemplate = new RestTemplate();
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/cleanNotifications").queryParam("userId",user.getUserid()).queryParam("groupId",groupId);
        restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.DELETE, null, Object.class);
        uriBuilder = UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/group/getNotificationsCount").queryParam("userId", user.getUserid());
        ResponseEntity<String> c = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });
        template.convertAndSend("/groupsNot/" + user.getUserid(),c.getBody());
    }


    @GetMapping("user/friendshipNotifications")
    public List<FriendshipNotifications> friendshipNotifications(ServletRequest req) {
        RestTemplate restTemplate = new RestTemplate();
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        String adderName=this.jwtTokenProvider.getUsername((HttpServletRequest) req);
        User user=us.findByEmail(adderName);
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/getFriendshipNotifications").queryParam("userId",user.getUserid());
        return restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<List<FriendshipNotifications>>() {}).getBody();
    }



    @DeleteMapping("user/ignoreNotifications")
    public void ignoreFriend(@RequestParam Long notificationId) {
        String host=microservices.getHost();
        String port=microservices.getUserAndgroupsPort();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder =UriComponentsBuilder.fromHttpUrl("http://"+host+":"+port+"/ignoreNotification").queryParam("notificationId",notificationId);
        restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.DELETE,null,Object.class);
    }

}