package Models;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class AdvertisementModel {
	
	
	private Long advertisementId;
	private Long authorId;
	private String advertisementName;
	private LocalDateTime deadline;
	private String description;
	private LocalDateTime dateOfPublication;
	private String budget;
	private String section;
	private String imageKey;
	private String content;
	

	
	
	
	@Override
	public String toString() {
		return "AdvertisementModel [advertisementId=" + advertisementId + ", authorId=" + authorId
				+ ", advertisementName=" + advertisementName + ", deadline=" + deadline + ", description=" + description
				+ ", dateOfPublication=" + dateOfPublication + ", budget=" + budget + ", section=" + section
				+ ", imageKey=" + imageKey + "]";
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public LocalDateTime getDeadline() {
		return deadline;
	}
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getImageKey() {
		return imageKey;
	}
	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
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