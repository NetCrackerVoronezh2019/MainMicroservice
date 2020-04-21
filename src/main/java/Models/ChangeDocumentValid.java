package Models;

import javax.validation.constraints.NotNull;

public class ChangeDocumentValid {

	@NotNull
	private Long id;
	@NotNull
	private Boolean validation;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getValidation() {
		return validation;
	}
	public void setValidation(Boolean validation) {
		this.validation = validation;
	}
	
	
}
