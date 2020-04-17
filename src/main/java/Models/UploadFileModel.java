package Models;

import javax.validation.constraints.NotNull;

public class UploadFileModel {
	
	public String key;
	@NotNull
	public String content;
	public String bucket;
	
	public UploadFileModel(String key,String content)
	{
		this.key=key;
		this.content=content;
	}

}
