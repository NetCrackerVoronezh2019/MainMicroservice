package Models;

public class UploadFilesModel {
	public UploadFileModel[] allFiles;
	
	public UploadFilesModel(int count)
	{
		allFiles=new UploadFileModel[count];
	}
}
