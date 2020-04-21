package Models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.mainmicroservice.mainmicroservice.Entities.User;

public class UserPageModel {
	private Long userId;
	private String firstname;
	private String lastname;
	private double reiting;
	private String email;
	private String educationLevel;
	private String gender;
	private LocalDateTime lastTimeWasONLINE;
	private String birthDate;
	private String aboutMe;
	private String roleName;
	private String userImageKey;
	private Boolean isOnline;
	private List<String> documentKeys;
	
	
	public static UserPageModel UserToModel(User user)
	{
		UserPageModel model=new UserPageModel();
		model.setUserId(user.getUserId());
		model.setFirstname(user.getFirstname());
		model.setLastname(user.getLastname());
		model.setReiting(user.getReiting());
		model.setEmail(user.getEmail());
		model.setLastTimeWasONLINE(user.getLastTimeWasONLINE());
		model.setAboutMe(user.getAboutMe());
		model.setUserImageKey(user.getUserImageKey());
		model.setBirthDate(user.getBirthDate().toString());
		model.setRoleName(user.getRole().getRoleName());
		model.setGender(user.getGender().toString());
		LocalDateTime t=model.getLastTimeWasONLINE();
		model.setDocumentKeys(user.documentsKeys());
		if(t!=null)
		{
			t=t.plusMinutes(2);
			
			if(LocalDateTime.now().isAfter(t))
				model.setIsOnline(false);
			else
				model.setIsOnline(true);
		}
		return model;
	}
	
	
	public List<String> getDocumentKeys() {
		return documentKeys;
	}

	public void setDocumentKeys(List<String> documentKeys) {
		this.documentKeys = documentKeys;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public double getReiting() {
		return reiting;
	}
	public void setReiting(double reiting) {
		this.reiting = reiting;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public LocalDateTime getLastTimeWasONLINE() {
		return lastTimeWasONLINE;
	}
	public void setLastTimeWasONLINE(LocalDateTime lastTimeWasONLINE) {
		this.lastTimeWasONLINE = lastTimeWasONLINE;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAboutMe() {
		return aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String role) {
		this.roleName = role;
	}
	public String getUserImageKey() {
		return userImageKey;
	}
	public void setUserImageKey(String userImageKey) {
		this.userImageKey = userImageKey;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	
	
	
	
	
}
