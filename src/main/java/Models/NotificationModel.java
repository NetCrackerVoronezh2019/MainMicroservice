package Models;

import Models.Enums.AdvertisementNotificationType;

public class NotificationModel {
	
	private Long notificationId;
	private String senderUsername;
	private String addresseeUsername;
	private Long senderId;
	private Long advertisementId;
	private String advertisementName;
	private Long addresseeId;
	private AdvertisementNotificationType type;
	private String message;

	
	
	public String getAdvertisementName() {
		return advertisementName;
	}

	public void setAdvertisementName(String advertisementName) {
		this.advertisementName = advertisementName;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}

	public String getAddresseeUsername() {
		return addresseeUsername;
	}

	public void setAddresseeUsername(String addresseeUsername) {
		this.addresseeUsername = addresseeUsername;
	}

	public void generateMessage()
	{
		if(type==AdvertisementNotificationType.TAKE_ADVERTISEMENT)
			this.setMessage("хочет получить объявление - ");
		else
			if(type==AdvertisementNotificationType.ACCEPTED_TAKE_ADVERTISEMENT)
				this.setMessage("принял ваш запрос - ");
			else
				if(type==AdvertisementNotificationType.REJECTED_TAKE_ADVERTISEMENT)
					this.setMessage("оклонил ваш запрос");
		}
	
	
	
	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Long getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}
	public Long getAddresseeId() {
		return addresseeId;
	}
	public void setAddresseeId(Long addresseeId) {
		this.addresseeId = addresseeId;
	}
	public AdvertisementNotificationType getType() {
		return type;
	}
	public void setType(AdvertisementNotificationType type) {
		this.type = type;
	}
	
	
	
}
