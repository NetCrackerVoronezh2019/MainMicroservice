package Models;

public class SendAdvertisementNotificationModel {
	
	private String roleName;
	private Long AdvertisementId;
	private Long senderId;
	
	
	
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getAdvertisementId() {
		return AdvertisementId;
	}
	public void setAdvertisementId(Long advertisementId) {
		AdvertisementId = advertisementId;
	}
	
	
	
}
