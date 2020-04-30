package Models;

import Models.Enums.OrderStatus;

public class MyOrderModel {

	public String role;
	public Long myId;
	public Long orderId;
	public OrderStatus status;
	
	
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getRole() {
		
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getMyId() {
		return myId;
	}
	public void setMyId(Long myId) {
		this.myId = myId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
