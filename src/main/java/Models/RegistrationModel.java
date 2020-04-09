package Models;

import java.util.*;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import Models.Enums.EducationLevel;
import Models.Enums.Gender;

public class RegistrationModel {
	
	public String firstname;
	public String lastname;
	public String email;
	public String password;
	public String role;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date birthDate;
	public List<String> allFiles=new ArrayList<String>();
	public Gender gender;
	public String aboutMe;
	
	public EducationLevel education;
	
	@Override
	public String toString() {
		return "RegistrationModel [firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", password=" + password + ", role=" + role + ", birthDate=" + birthDate + ", gender=" + gender
				+ ", aboutMe=" + aboutMe + ", education=" + education + "]";
	}
	
	
	
	
	
	
	
	
}
