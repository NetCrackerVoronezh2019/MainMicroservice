package Models;

import java.util.List;

public class CompleteOrderModel {

	private Long orderId;
	private Long userId;
	private String roleName;
	private List<FileModel> allFiles;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
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
	public List<FileModel> getAllFiles() {
		return allFiles;
	}
	public void setAllFiles(List<FileModel> allFiles) {
		this.allFiles = allFiles;
	}
	
	
}
