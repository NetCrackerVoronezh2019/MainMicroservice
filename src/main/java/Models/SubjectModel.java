package Models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SubjectModel {

	private Long id;
	private String name;
	private String translateName;
	private String url;
	private boolean isChecked;
	
	
	
	@JsonGetter("isChecked")
	public boolean isChecked() {
		
		return isChecked;
	}
	
	@JsonSetter("isChecked")
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTranslateName() {
		return translateName;
	}
	public void setTranslateName(String translateName) {
		this.translateName = translateName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
