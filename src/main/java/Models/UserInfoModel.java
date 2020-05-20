package Models;

import java.time.LocalDateTime;

public class UserInfoModel {
 public String userName;
 public String roleName;
 public Long userId;
 public LocalDateTime banTime;
 
 public UserInfoModel(String name,Long id)
 {
	 userId=id;
	 userName=name;
 }
 public UserInfoModel(String name,String roleName)
 {
	 this.userName=name;
	 this.roleName=roleName;
 }
 
 public UserInfoModel(String name)
 {
	 userName=name;
 }
 
 public UserInfoModel() {}
}
