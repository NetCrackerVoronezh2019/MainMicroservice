package Models;

import Models.Enums.AdvertisementNotificationType;

public class CertificationNotModel {

	private Long senderId;
	private Long addresseeId;
	private String certificateName;
	private AdvertisementNotificationType type;
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public Long getAddresseeId() {
		return addresseeId;
	}
	public void setAddresseeId(Long addresseeId) {
		this.addresseeId = addresseeId;
	}
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public AdvertisementNotificationType getType() {
		return type;
	}
	public void setType(AdvertisementNotificationType type) {
		this.type = type;
	}
	
	
	
}
