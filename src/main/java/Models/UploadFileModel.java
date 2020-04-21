package Models;

import javax.validation.constraints.NotNull;

public class UploadFileModel {
	
	public String key;
	@NotNull
	public String content;
	public String bucket;
	public String contentType;
	
	public UploadFileModel(String key,String content,String contentType)
	{
		this.key=key;
		this.content=content;
		this.contentType=contentType;
	}

}
