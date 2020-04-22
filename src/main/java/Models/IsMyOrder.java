package Models;

import javax.validation.constraints.NotNull;

public class IsMyOrder {
	
	@NotNull
	private Long advertisementId; 
	
	private Long userId;

	public Long getAdvertisementId() {
		return advertisementId;
	}

	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
}
