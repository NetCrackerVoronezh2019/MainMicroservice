package Models;



public class OrderModel {

	private Long orderId;
	private Long customerId;
	private Long freelancerId;
	private Long advertisementId;
	private String advertisementName;
	private String status;
	private String nextStatus;
	private String freelancerFIO;
	private String customerFIO;
	private double starsForWork;
	private String comment;
	

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public double getStarsForWork() {
		return starsForWork;
	}
	public void setStarsForWork(double starsForWork) {
		this.starsForWork = starsForWork;
	}
	public String getFreelancerFIO() {
		return freelancerFIO;
	}
	public void setFreelancerFIO(String freelancerFIO) {
		this.freelancerFIO = freelancerFIO;
	}
	public String getCustomerFIO() {
		return customerFIO;
	}
	public void setCustomerFIO(String customerFIO) {
		this.customerFIO = customerFIO;
	}

	public String getNextStatus() {
		return nextStatus;
	}
	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getFreelancerId() {
		return freelancerId;
	}
	public void setFreelancerId(Long freelancerId) {
		this.freelancerId = freelancerId;
	}
	public Long getAdvertisementId() {
		return advertisementId;
	}
	public void setAdvertisementId(Long advertisementId) {
		this.advertisementId = advertisementId;
	}
	public String getAdvertisementName() {
		return advertisementName;
	}
	public void setAdvertisementName(String advertisementName) {
		this.advertisementName = advertisementName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
