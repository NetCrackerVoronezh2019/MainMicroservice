package Models;

import Models.Enums.AdvertisementStatus;

public class UserAdvertisementsModel {

	private Long userId;
	private AdvertisementStatus status;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public AdvertisementStatus getStatus() {
		return status;
	}
	public void setStatus(AdvertisementStatus status) {
		this.status = status;
	}
	
	
}
