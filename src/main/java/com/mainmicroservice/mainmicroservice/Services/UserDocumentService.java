package com.mainmicroservice.mainmicroservice.Services;




import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mainmicroservice.mainmicroservice.Entities.UserDocument;
import com.mainmicroservice.mainmicroservice.Repositories.UserDocumentRepository;

@Service
public class UserDocumentService {

	
	@Autowired 
	private UserDocumentRepository udRepository;
	
	public UserDocument save(UserDocument document)
	{
		return this.udRepository.save(document);
	}
		
	public List<UserDocument> findAllValidDocuments()
	{
		return this.udRepository.findAll()
			.stream()
			.filter(d->d.getIsValid().equals(Boolean.TRUE))
			.collect(Collectors.toList());
			
	}
	
	public List<UserDocument> findAllUnValidDocuments()
	{
		return this.udRepository.findAll()
				.stream()
				.filter(d->d.getIsValid().equals(Boolean.FALSE))
				.collect(Collectors.toList());	
	}
	
	
	public UserDocument findById(Long id)
	{
		return this.udRepository.findById(id).get();
	}
}
