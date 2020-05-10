package Models;

import java.util.ArrayList;
import java.util.List;

public class CertificateFileModel {

	private String section;
	private List<FileModel> allFiles=new ArrayList<FileModel>();
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public List<FileModel> getAllFiles() {
		return allFiles;
	}
	public void setAllFiles(List<FileModel> allFiles) {
		this.allFiles = allFiles;
	}
	
	
	
}
