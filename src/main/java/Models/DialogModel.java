package Models;

import java.util.Date;

public class DialogModel {

	 private String name ;
	 private Date creationDate;
	 private Date lastMessageDate;
	 private Integer creatorId;
	 private Integer dialogId;
	 private Integer countNotification;
	 private String type;
	 private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getLastMessageDate() {
		return lastMessageDate;
	}

	public void setLastMessageDate(Date lastMessageDate) {
		this.lastMessageDate = lastMessageDate;
	}

	public Integer getCountNotification() {
		return countNotification;
	}

	public void setCountNotification(Integer countNotification) {
		this.countNotification = countNotification;
	}

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
