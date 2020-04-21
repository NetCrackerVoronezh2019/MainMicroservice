package Models;

import Models.Enums.AdvertisementNotificationType;
import Models.Enums.NotificationResponseStatus;
import Models.Enums.NotificationStatus;

public class NotificationModel {
	
	private Long notificationId;
	private String senderUsername;
	private String addresseeUsername;
	private Long senderId;
	private Long advertisementId;
	private String advertisementName;
	private Long addresseeId;
	private AdvertisementNotificationType type;
	private NotificationResponseStatus responseStatus;
	private NotificationStatus status;
	private String message;
	private String senderFIO;
	private Long orderId;
	
	
	public String getSenderFIO() {
		return senderFIO;
	}

	public void setSenderFIO(String senderFIO) {
		this.senderFIO = senderFIO;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public NotificationResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(NotificationResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

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
			this.setMessage("хочет получить объявление");
		else
			if(type==AdvertisementNotificationType.ACCEPTED_TAKE_ADVERTISEMENT)
				this.setMessage("принял ваш запрос");
			else
				if(type==AdvertisementNotificationType.REJECTED_TAKE_ADVERTISEMENT)
					this.setMessage("отклонил ваш запрос");
				else
					if(type==AdvertisementNotificationType.RECEIVE_SERVICE)
						this.setMessage("хочет получить услугу");
					else
						if(type==AdvertisementNotificationType.ACCEPTED_RECEIVE_SERVICE)
							this.setMessage("готов оказать услугу");
						else
							if(type==AdvertisementNotificationType.REJECTED_RECEIVE_SERVICE)
								this.setMessage("не готов оказать услугу");
							else
								if(type==AdvertisementNotificationType.CHANGE_ORDER_STATUS_TO_INPROGRESS)
									this.setMessage("изменил статус заказа на <<В процессе>>");
								else
									if(type==AdvertisementNotificationType.CHANGE_ORDER_STATUS_TO_COMPLETED)
									this.setMessage("изменил статус заказа на <<Завершен>>");
								else
									if(type==AdvertisementNotificationType.CHANGE_REITING)
										this.setMessage("оценил вашу работу");
									
						
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

	@Override
	public String toString() {
		return "NotificationModel [notificationId=" + notificationId + ", senderUsername=" + senderUsername
				+ ", addresseeUsername=" + addresseeUsername + ", senderId=" + senderId + ", advertisementId="
				+ advertisementId + ", advertisementName=" + advertisementName + ", addresseeId=" + addresseeId
				+ ", type=" + type + ", message=" + message + "]";
	}
	
	
	
}
