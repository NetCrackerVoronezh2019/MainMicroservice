package com.mainmicroservice.mainmicroservice.Services;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import com.mainmicroservice.mainmicroservice.Entities.User;
import com.mainmicroservice.mainmicroservice.Entities.UserDocument;
import com.mainmicroservice.mainmicroservice.Repositories.UserRepository;

import Models.CertificateFileModel;
import Models.FileModel;
import Models.UploadFileModel;
import Models.UploadFilesModel;
import Models.Enums.TeacherStatus;


@Service

public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDocumentService udService;

	public List<User> getAllUsers()
	{
		return this.userRepository.findAll();
	}
	public User getUserById(Long id)
	{
		return userRepository.findByUserId(id);
	}
	
	public void saveChanges(User user) {
		userRepository.save(user);
	}
	
	public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
       
        return result;
    }
	
	public User getUserByActivateCode(String code) 
	{
		try
		{
			return userRepository.findByActivateCode(code);
		}
		catch(Exception ex)
		{
			return null;
		}
		
	}
	
	public User addNewUser(User us)
	{
		return userRepository.save(us);
	}
	
	public User setTeacherStatus(User user)
	{
		List<UserDocument> documents=user.getDocuments();
		documents=documents.stream()
				 .filter(doc->doc.getIsValid()==Boolean.TRUE)
				 .collect(Collectors.toList());
		
		if(documents.size()>0)
			user.setTeacherStatus(TeacherStatus.CERTIFIED_SPECIALIST);
		
		if(documents.size()==0)
			user.setTeacherStatus(TeacherStatus.NOT_A_CERTIFIED_SPECIALIST);
		
		
		return user;
		
	}
	
	
   public UploadFilesModel setUserDocuments(User user,List<CertificateFileModel> certificateFiles)
   {
		UploadFilesModel files=new UploadFilesModel();
		
	   for(int i=0;i<certificateFiles.size();i++)
	   {
		   List<FileModel> allFiles=certificateFiles.get(i).getAllFiles();
		   for(int j=0;j<allFiles.size();j++)
		   {
			   UserDocument doc=new UserDocument();
			   doc.setIsValid(Boolean.FALSE);
			   doc.setUser(user);
			   doc.setSubject(certificateFiles.get(i).getSection());
			   String key="user"+user.getUserid()+"_document"+allFiles.get(j).name;
			   doc.setDocumentKey(key);
			   this.udService.save(doc);
			   files.allFiles.add(new UploadFileModel(doc.getDocumentKey(),allFiles.get(j).content,allFiles.get(j).contentType));
		   }
	   }
	   return files;
   }

	
}
