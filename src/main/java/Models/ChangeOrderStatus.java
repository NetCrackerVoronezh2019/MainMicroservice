package Models;

import Models.Enums.OrderStatus;

public class ChangeOrderStatus {

	private Long orderId;
	private Long userId;
	private String roleName;
	//private Long customerId;
    //private Long freelancerId;
    
   
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	
	
}
