package Models;

import com.mainmicroservice.mainmicroservice.Entities.UserDocument;

public class ChangeUserDocument {
	
	private Long userId;
	private UserDocument userDocument;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public UserDocument getUserDocument() {
		return userDocument;
	}
	public void setUserDocument(UserDocument userDocument) {
		this.userDocument = userDocument;
	}
	
	
}
