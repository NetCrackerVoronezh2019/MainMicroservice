package Models;

import java.util.Date;
import java.util.List;

public class MessagesModel {

	private Integer messageId;
    private String text;
    private Date date;
    private Integer dialog;
    private UserModel sender;
    private boolean modified;
	private boolean isReaded;
	private boolean readBySomebody;
	private List<String> files;
	private List<String> names;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isReadBySomebody() {
		return readBySomebody;
	}

	public void setReadBySomebody(boolean readBySomebody) {
		this.readBySomebody = readBySomebody;
	}

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
	public boolean isReaded() {
		return isReaded;
	}
	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
	
}
