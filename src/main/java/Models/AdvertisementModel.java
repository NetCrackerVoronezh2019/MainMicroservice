package Models;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class AdvertisementModel {
	
	
	private Long advertisementId;
	private Long authorId;
	private String advertisementName;
	private String section;
	private LocalDateTime deadline;
	private String description;
	private LocalDateTime dateOfPublication;
	
	
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
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getDeadline() {
		return deadline.toString();
	}
	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
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