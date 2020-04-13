package com.mainmicroservice.mainmicroservice.Entities;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;
import Jacson.Views;
import Models.Enums.EducationLevel;
import Models.Enums.Gender;

@Entity
@Table(name = "USERS")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="USERID")
	private Long userId;
	
	@Column(name="USERIMAGEKEY")
	private String userImageKey;
	
	@Column(name="ISACTIVATE")
	@JsonView(Views.UserInfoForChangeProps.class)
	public boolean IsActivate;
	
	@Column(name="ACTIVATECODE")
	private String activateCode;
	
	@Size(min=4, max=20)
	@Column(name="FIRSTNAME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String firstname;
	
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="LASTNAME")
	@Size(min=4, max=20)
	private String lastname;
	
	@Column(name="REITING")
	@JsonView(Views.UserInfoForChangeProps.class)
	private double reiting;
	
	@Email
	@Column(name="EMAIL",unique=true)
	@JsonView(Views.UserInfoForChangeProps.class)
	private String email;
	
	@Column(name="EDUCATIONLEVEL")
	@Enumerated(EnumType.STRING) 
	private EducationLevel educationLevel;
	
	
	@Column(name="GENDER")
	@JsonView(Views.UserInfoForChangeProps.class)
	@Enumerated(EnumType.STRING) 
	private Gender gender;
	
	@Column(name="PASSWORD")
	private String password;
	
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="ISDELETED")
	private Boolean isDeleted;
	
	@Column(name="LASTTIMEWASONLINE")
	@JsonView(Views.UserInfoForChangeProps.class)
	private LocalDateTime lastTimeWasONLINE;
	
	@Column(name="CertificationKeys")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String certificationKeys;
	
	@Column(name="BIRTHDATE")
	@JsonView(Views.UserInfoForChangeProps.class)
	private Date birthDate;
	
	@Column(name="ABOUTME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String aboutMe;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVICEWEBSITE", nullable = false)
    private Service service;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonView(Views.UserInfoForChangeProps.class)
    @JoinColumn(name = "ROLEID", nullable = false)
    private Role role;
	
	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		
		this.isDeleted = isDeleted;
	
		
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserImageKey() {
		return userImageKey;
	}

	public void setUserImageKey(String userImageKey) {
		this.userImageKey = userImageKey;
	}

	public String getCertificationKeys() {
		return certificationKeys;
	}

	public void setCertificationKeys(String certificationKeys) {
		this.certificationKeys = certificationKeys;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	
	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public EducationLevel getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(EducationLevel educationLevel) {
		this.educationLevel = educationLevel;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getLastTimeWasONLINE() {
		return lastTimeWasONLINE;
	}

	public void setLastTimeWasONLINE(LocalDateTime lastTimeWasONLINE) {
		this.lastTimeWasONLINE = lastTimeWasONLINE;
	}
	
	
	public double getReiting() {
		return reiting;
	}

	public void setReiting(double reiting) {
		this.reiting = reiting;
	}

	public Role getRole() {
		return this.role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	 
	 
	public long getUserid() {
		return userId;
	}
	 
	public void setUserid(long userid) {
		userId = userid;
	}

	public boolean isIsActivate() {
		return IsActivate;
	}
	
	public void setIsActivate(boolean isActivate) {
		IsActivate = isActivate;
	}
	 
	public String getActivateCode() {
		return activateCode;
	}
	
	public void setActivateCode(String activateCode) {
		this.activateCode = activateCode;
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
	 
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	 
	public void setPassword(String password) {
		this.password = password;
	}
	 
	public String getService() {
		if(this.service==null)
		 {
			return "none";
		 }
		return this.service.getServiceWebSite();
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	
	public String[] setCerteficationKeys(List<String> keys)
	{
		String str="";
		String[] arr=new String[keys.size()];
		String newKey="";
		for(int i=0;i<keys.size();i++)
		{
		  	newKey="user"+this.getUserid()+"_certification"+i;
		  	arr[i]=newKey;
		  	str+=newKey+",";
		}
		
		this.setCertificationKeys(str);
		return arr;
	}
	

}
