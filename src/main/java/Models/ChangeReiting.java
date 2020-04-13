package Models;

import javax.validation.constraints.NotNull;

public class ChangeReiting {
	
	@NotNull
	private double reiting;
	private Long freelancerId;
	private Long advertisementId;
	private Long orderId;
	private Long customerId;
	
	
	

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getAdvertisementId() {
		return advertisementId;
	}

	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}

	public Long getFreelancerId() {
		return freelancerId;
	}

	public void setFreelancerId(Long freelancerId) {
		this.freelancerId = freelancerId;
	}

	public double getReiting() {
		return reiting;
	}

	public void setReiting(double reiting) {
		this.reiting = reiting;
	}
	
	
}
