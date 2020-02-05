package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class AdvertisementModel {
	@JsonProperty(access=Access.READ_ONLY)
	private Long userId;
	private String advertisementName;
	private String advertisementSection;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime deadlineDate;
	private String description;
	public String getAdvertisementName() {
		return advertisementName;
	}
		
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public void setAdvertisementName(String advertisementName) {
		this.advertisementName = advertisementName;
	}
	public String getAdvertisementSection() {
		return advertisementSection;
	}
	public void setAdvertisementSection(String advertisementSection) {
		this.advertisementSection = advertisementSection;
	}
	public String getDeadlineDate() {
		return deadlineDate.toString();
	}
	
	@JsonSetter("deadlineDate")
	public void setDate(LocalDateTime deadlineDate)
	{
		this.deadlineDate=deadlineDate;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
