package Models;

import java.util.Date;

public class MessagesModel {

	private Integer messageId;
    private String text;
    private Date date;
    private Integer dialog;
    private UserModel sender;
    private boolean isModified;
	private boolean isReaded;
	public Integer getMessageId() {
		return messageId;
	}
	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getDialog() {
		return dialog;
	}
	public void setDialog(Integer dialog) {
		this.dialog = dialog;
	}
	public UserModel getSender() {
		return sender;
	}
	public void setSender(UserModel sender) {
		this.sender = sender;
	}
	public boolean isModified() {
		return isModified;
	}
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	public boolean isReaded() {
		return isReaded;
	}
	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
	
}
