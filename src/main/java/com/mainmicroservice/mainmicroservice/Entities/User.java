package com.mainmicroservice.mainmicroservice.Entities;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.*;
import Jacson.Views;
import Models.CertificateFileModel;
import Models.FileModel;
import Models.Enums.BlockType;
import Models.Enums.EducationLevel;
import Models.Enums.Gender;
import Models.Enums.TeacherStatus;

@Entity
@Table(name = "USERS")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "user_index", type = "users")
public class User {

	
	@Id
	@org.springframework.data.annotation.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="USERID")
	private Long userId;
	
	@Column(name="USERIMAGEKEY")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String userImageKey;
	
	@Column(name="ISACTIVATE")
	@JsonView(Views.UserInfoForChangeProps.class)
	public boolean IsActivate;
	
	@Column(name="ACTIVATECODE")
	private String activateCode;
	
	
	@Column(name="FIRSTNAME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String firstname;
	
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="LASTNAME")

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
	@JsonView(Views.UserInfoForChangeProps.class)
	private EducationLevel educationLevel;
	
	
	@Column(name="GENDER")
	@JsonView(Views.UserInfoForChangeProps.class)
	@Enumerated(EnumType.STRING) 
	private Gender gender;
	
	@Column(name="TEACHERSTATUS")
	@JsonView(Views.UserInfoForChangeProps.class)
	@Enumerated(EnumType.STRING) 
	private TeacherStatus teacherStatus;
	
	@JsonIgnore
	@Column(name="PASSWORD")
	private String password;
	
	@JsonView(Views.UserInfoForChangeProps.class)
	@Column(name="ISDELETED")
	private Boolean isDeleted;
	

	@Column(name="LASTTIMEWASONLINE")
	@JsonView(Views.UserInfoForChangeProps.class)
	private LocalDateTime lastTimeWasONLINE;
	

	@Column(name="BIRTHDATE")
	@JsonView(Views.UserInfoForChangeProps.class)
	private Date birthDate;
	
	@Column(name="ABOUTME")
	@JsonView(Views.UserInfoForChangeProps.class)
	private String aboutMe;
	
	@Column(name="cancellationOfTheBan")
	@JsonView(Views.UserInfoForChangeProps.class)
	private LocalDateTime cancellationOfTheBan;	
	
	@Column(name="BLOCKTYPE")
	@Enumerated(EnumType.STRING) 
	@JsonView(Views.UserInfoForChangeProps.class)
	private BlockType blockType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonView(Views.UserInfoForChangeProps.class)
    @JoinColumn(name = "ROLEID", nullable = false)
    private Role role;
	
	
	
	@OneToMany(cascade = CascadeType.ALL,
		    fetch = FetchType.EAGER,
		    mappedBy = "user")
	private List<UserDocument> documents=new ArrayList<>();
	
	
	//@JsonGetter("documents")
	public List<String> documentsKeys()
	{
		return this.getDocuments().stream()
			.map(d->d.getDocumentKey())
			.collect(Collectors.toList());
	}
	
	
	
	public List<UserDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<UserDocument> documents) {
		this.documents = documents;
	}

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

	
	public BlockType getBlockType() {
		return blockType;
	}



	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}



	public void setUserImageKey(String userImageKey) {
		this.userImageKey = userImageKey;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	
	public TeacherStatus getTeacherStatus() {
		return teacherStatus;
	}



	public void setTeacherStatus(TeacherStatus teacherStatus) {
		this.teacherStatus = teacherStatus;
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
	
	
	public LocalDateTime getCancellationOfTheBan() {
		return cancellationOfTheBan;
	}

	public void setCancellationOfTheBan(LocalDateTime cancellationOfTheBan) {
		this.cancellationOfTheBan = cancellationOfTheBan;
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
	 


}
