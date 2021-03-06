package Models;

import java.util.Date;

import com.mainmicroservice.mainmicroservice.Entities.User;

public class UserModel {

	private Integer userId;
	private String name;
	private String image;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserModel(User user) {
		userId = (int)user.getUserid();
		name = user.getLastname() + ' ' + user.getFirstname();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public UserModel() {}
	
	
}
