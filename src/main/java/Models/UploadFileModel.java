package Models;

public class UploadFileModel {
	
	public String key;
	public String content;
	public String bucket;
	
	public UploadFileModel(String key,String content)
	{
		this.key=key;
		this.content=content;
	}

}
