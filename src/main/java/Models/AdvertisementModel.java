package Models;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import Models.Enums.AdvertisementStatus;
import Models.Enums.AdvertisementType;

public class AdvertisementModel {
	
	
	private Long advertisementId;
	private Long authorId;
	private String advertisementName;
	private LocalDateTime deadline;
	private String description;
	private LocalDateTime dateOfPublication;
	private String budget;
	private String section;
	private FileModel coverImage;
	private String coverImageKey;
	private String content;
	private String authorRole;
	private String firstName;
	private String surName;
	private AdvertisementStatus status;
	private AdvertisementType type;
	private Tag[] tags;
	private FileModel[] allFiles;
	private AttachmentModel[] attachments;

	
	
	public AdvertisementStatus getStatus() {
		return status;
	}
	public void setStatus(AdvertisementStatus status) {
		this.status = status;
	}
	public String getCoverImageKey() {
		return coverImageKey;
	}
	public void setCoverImageKey(String coverImageKey) {
		this.coverImageKey = coverImageKey;
	}

	public AttachmentModel[] getAttachments() {
		return attachments;
	}
	public void setAttachments(AttachmentModel[] attachments) {
		this.attachments = attachments;
	}
	public Tag[] getTags() {
		return tags;
	}
	public void setTags(Tag[] tags) {
		this.tags = tags;
	}
	
	public FileModel[] getAllFiles() {
		return allFiles;
	}
	public void setAllFiles(FileModel[] allFiles) {
		this.allFiles = allFiles;
	}
	
	
	public FileModel getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(FileModel coverImage) {
		this.coverImage = coverImage;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public AdvertisementType getType() {
		return type;
	}
	public void setType(AdvertisementType type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public String getAuthorRole() {
		return authorRole;
	}
	public void setAuthorRole(String authorRole) {
		this.authorRole = authorRole;
	}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	public LocalDateTime getDeadline() {
		return deadline;
	}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public Long getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	public String getAdvertisementName() {
		return advertisementName;
	}
	public void setAdvertisementName(String advertisementName) {
		this.advertisementName = advertisementName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
   public String getDateOfPublication() {
	   if(dateOfPublication!=null)
		return dateOfPublication.toString();
	    return null;
	}
	public void setDateOfPublication(LocalDateTime dateOfPublication) {
		this.dateOfPublication = dateOfPublication;
	}
	
	
	
}