package Models;

import java.util.Date;

public class DialogModel {

	 private String name ;
	 private Date creationDate;
	 private Integer creatorId;
	 private Integer dialogId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	public Integer getDialogId() {
		return dialogId;
	}
	public void setDialogId(Integer dialogId) {
		this.dialogId = dialogId;
	}
	 
	 
}
